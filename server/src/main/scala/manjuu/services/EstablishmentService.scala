package manjuu.services

import manjuu.client.FSAClient
import manjuu.domain.{AuthoritySummary, RatingSummary}

import cats.effect.IO
import cats.effect.kernel.Resource
import io.circe.Json
import org.http4s.dsl.impl.Auth

trait EstablishmentService[F[_]]:
  def hygieneRatings(id: Int, establishments: Int): F[Option[AuthoritySummary]]

object EstablishmentService:
  def apply[F[_]](using ev: EstablishmentService[F]): EstablishmentService[F] = ev

  def stub(): EstablishmentService[IO] =
    new EstablishmentService[IO] {
      val stubData                                                                   = Map(
        1 -> AuthoritySummary(
          name = "Test Authority",
          ratings = RatingSummary.Standard("20.0", "20.0", "20.0", "10.0", "10.0", "10.0", "10.0")
        ),
        2 -> AuthoritySummary(
          "Another Authority",
          ratings = RatingSummary.Standard("25.0", "20.0", "20.0", "10.0", "10.0", "10.0", "5.0")
        ),
        3 -> AuthoritySummary(
          "Third Authority",
          RatingSummary.Scottish("40.0", "40.0", "20.0")
        ),
        4 -> AuthoritySummary(
          "Fourth Authority",
          RatingSummary.Scottish("30.0", "40.0", "30.0")
        ),
        5 -> AuthoritySummary(
          "Fifth and final authority",
          RatingSummary.Standard("10.0", "20.0", "20.0", "10.0", "10.0", "15.0", "15.0")
        )
      )
      def hygieneRatings(id: Int, establishments: Int): IO[Option[AuthoritySummary]] = IO.pure(
        stubData.get(id)
      )
    }

  def impl(client: Resource[IO, FSAClient[IO, Json]]): EstablishmentService[IO] =
    new EstablishmentService[IO]:
      def hygieneRatings(id: Int, establishments: Int): IO[Option[AuthoritySummary]] =
        return client
          .use(_.fetch(s"authorities/$id?pageSize=$establishments"))
          .map(_.as[AuthoritySummary].toOption)
