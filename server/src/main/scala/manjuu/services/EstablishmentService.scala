package manjuu.services

import manjuu.client.FSAClient
import manjuu.domain.AuthoritySummary
import manjuu.services.util.EstablishmentParser
import manjuu.services.util.RatingsFormatter

import cats._
import cats.data._
import cats.effect._
import cats.effect.IO
import cats.effect.kernel.Resource
import cats.implicits._
import cats.syntax.all._
import io.circe.Json
import org.http4s.EntityDecoder
import org.http4s.dsl.impl.Auth
import org.http4s.implicits._

trait EstablishmentService[F[_]]:
  def hygieneRatings(id: Int): F[Option[AuthoritySummary]]

object EstablishmentService:
  def apply[F[_]](using ev: EstablishmentService[F]): EstablishmentService[F] = ev

  case class AuthorityResponse(
    LocalAuthorityId: Int,
    Name: String,
    EstablishmentCount: Int,
    Url: String
  )
  object AuthorityResponse:
    import io.circe.Codec
    import io.circe.generic.semiauto.deriveCodec
    import org.http4s.circe._
    import org.http4s.circe.CirceEntityDecoder._
    given Codec[AuthorityResponse] = deriveCodec

    given EntityDecoder[IO, AuthorityResponse] = jsonOf[IO, AuthorityResponse]

  def impl(
    client: FSAClient[IO, Json],
    parser: EstablishmentParser,
    formatter: RatingsFormatter
  ): EstablishmentService[IO] =
    new EstablishmentService[IO]:
      def hygieneRatings(id: Int): IO[Option[AuthoritySummary]] =
        val getAuthority = () => OptionT(client.get[AuthorityResponse](s"authorities/$id"))
        val path         = s"establishments"

        val getSummary = (a: AuthorityResponse) =>
          client
            .fetch(
              path,
              Map(
                "localAuthorityId" -> a.LocalAuthorityId.toString,
                "pageSize"         -> a.EstablishmentCount.toString
              )
            )
            .map(parser.countEstablishmentRatings)

        val resultT = for {
          authority      <- getAuthority()
          establishments <- OptionT.liftF(getSummary(authority))
          summary         = formatter.summariseRatings(establishments)
          ratings        <- OptionT.fromOption(summary.toOption)
        } yield AuthoritySummary(
          name = authority.Name,
          url = authority.Url,
          establishments = authority.EstablishmentCount,
          ratings = ratings
        )

        resultT.value
