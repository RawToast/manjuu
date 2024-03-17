package manjuu.services

import manjuu.client.{Cache, FSAClient}
import manjuu.domain.AuthoritySummary
import manjuu.services.util.{EstablishmentParser, FormatterError, RatingsFormatter}

import cats._
import cats.data._
import cats.effect.IO
import cats.effect.kernel.{Async, Resource}
import cats.implicits._
import io.circe.Json
import io.circe.syntax._
import org.http4s.EntityDecoder
import org.http4s.Uri.Path
import org.http4s.implicits._
import scala.concurrent.duration.FiniteDuration

enum EstablishmentServiceError:
  case AuthorityNotFound
  case InvalidRatings(message: String)

object EstablishmentServiceError:
  def fromFormatterError(err: FormatterError): EstablishmentServiceError =
    err match
      case FormatterError.NoRatings            => InvalidRatings("No ratings found")
      case FormatterError.InvalidData(message) =>
        InvalidRatings("Invalid ratings data, reason: " + message)

trait EstablishmentService[F[_]]:
  def hygieneRatings(id: Int): F[Either[EstablishmentServiceError, AuthoritySummary]]

object EstablishmentService:
  import EstablishmentServiceError._
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
      def hygieneRatings(id: Int): IO[Either[EstablishmentServiceError, AuthoritySummary]] =
        val getAuthority = () =>
          EitherT(
            client
              .get[AuthorityResponse](path"authorities" / id)
              .map(_.toRight[EstablishmentServiceError](AuthorityNotFound))
          )

        val path: Path = path"establishments"

        val getSummary = (authority: AuthorityResponse) =>
          client
            .fetch(
              path,
              Map(
                "localAuthorityId" -> authority.LocalAuthorityId.toString,
                "pageSize"         -> authority.EstablishmentCount.toString
              )
            )
            .map(parser.countEstablishmentRatings)

        val resultT = for {
          authority      <- getAuthority()
          establishments <- EitherT.liftF(getSummary(authority))
          summary         = formatter.summariseRatings(establishments).leftMap(fromFormatterError)
          ratings        <- EitherT.fromEither(summary)
        } yield AuthoritySummary(
          name = authority.Name,
          url = authority.Url,
          establishments = authority.EstablishmentCount,
          ratings = ratings
        )

        resultT.value

  def withCache[F[_]: Async](
    service: EstablishmentService[F],
    redisResource: Resource[F, Cache[F, String, Json]]
  ): EstablishmentService[F] =
    new EstablishmentService[F]:
      def makeKey(id: Int)                                                                = s"establishments:$id"
      def hygieneRatings(id: Int): F[Either[EstablishmentServiceError, AuthoritySummary]] =
        val key = makeKey(id)
        redisResource.use {
          redis =>
            for
              cachedData <- redis.get(key)
              data        = cachedData.flatMap(_.as[AuthoritySummary].toOption)

              summary <- data match
                           case Some(summary) => Monad[F].pure(summary.asRight)
                           case None          =>
                             service.hygieneRatings(id).flatTap {
                               case Right(summary) => redis.set(key, summary.asJson)
                               case _              => Applicative[F].unit
                             }
            yield summary
        }
