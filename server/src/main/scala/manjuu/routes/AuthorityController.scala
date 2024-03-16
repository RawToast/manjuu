package manjuu.routes

import manjuu.domain._
import manjuu.services._

import cats.Monad
import cats.effect._
import cats.effect.kernel.Async
import cats.implicits._
import ch.qos.logback.classic.Logger
import io.circe.syntax._
import org.http4s._
import org.http4s.Method.GET
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._

trait Routes[F[_]] {
  val routes: HttpRoutes[F]
}

object AuthorityController:

  def apply[F[_]](using ev: Routes[F]): Routes[F] = ev

  def impl[F[_]: Monad](
    authorityService: AuthorityService[F],
    establishmentService: EstablishmentService[F]
  ): Routes[F] = new Routes[F]:
    val dsl = new Http4sDsl[F] {}
    import dsl._

    object AuthoritySizeMatcher extends OptionalQueryParamDecoderMatcher[Int]("authSize")

    val routes = HttpRoutes.of[F] {
      case GET -> Root / "authority" =>
        for {
          authorities <- authorityService.authorities
          json         = authorities.asJson
          response    <- Ok(json)
        } yield response

      case GET -> Root / "authority" / IntVar(id) =>
        for {
          establishments <- establishmentService.hygieneRatings(id)
          response       <- establishments match
                              case Right(establishments)                                   =>
                                Ok(establishments.asJson)
                              case Left(EstablishmentServiceError.InvalidRatings(message)) =>
                                UnprocessableEntity(ErrorMessage(message).asJson)
                              case Left(EstablishmentServiceError.AuthorityNotFound)       =>
                                NotFound(ErrorMessage(s"Could not find an authority with id: $id").asJson)
        } yield response
    }
