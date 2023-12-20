package manjuu.services

import manjuu.client.FSAClient
import manjuu.domain.{AuthoritySummary, RatingSummary}
import manjuu.services.util.EstablishmentParser

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

  def stub(): EstablishmentService[IO] =
    new EstablishmentService[IO] {
      val stubData                                              = Map(
        1 -> AuthoritySummary(
          name = "Test Authority",
          url = "http://www.test-authority.gov.uk",
          establishments = 48,
          ratings = RatingSummary.Standard(20, 20, 20, 10, 10, 10, 10)
        ),
        2 -> AuthoritySummary(
          name = "Another Authority",
          url = "http://www.another-authority.gov.uk",
          establishments = 123,
          ratings = RatingSummary.Standard(25, 20, 20, 10, 10, 10, 5)
        ),
        3 -> AuthoritySummary(
          "http://www.third-authority.gov.uk",
          "Third Authority",
          663,
          RatingSummary.Scottish(40, 40, 20)
        ),
        4 -> AuthoritySummary(
          "http://www.fourth-authority.gov.uk",
          "Fourth Authority",
          6,
          RatingSummary.Scottish(30, 40, 30)
        ),
        5 -> AuthoritySummary(
          "http://www.fifth-authority.gov.uk",
          "Fifth and final authority",
          44,
          RatingSummary.Standard(10, 20, 20, 10, 10, 15, 15)
        )
      )
      def hygieneRatings(id: Int): IO[Option[AuthoritySummary]] = IO.pure(
        stubData.get(id)
      )
    }

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
    parser: EstablishmentParser
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
          ratings         = RatingSummary.fromMap(establishments)
        } yield AuthoritySummary(
          name = authority.Name,
          url = authority.Url,
          establishments = authority.EstablishmentCount,
          ratings = ratings
        )

        resultT.value
