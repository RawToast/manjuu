package manjuu.services

import manjuu.client.{Cache, FSAClient}
import manjuu.domain.Authority
import manjuu.services.util.AuthorityParser

import cats.Monad
import cats.effect.IO
import cats.effect.kernel.{Async, Resource}
import cats.implicits._
import dev.profunktor.redis4cats.RedisCommands
import io.circe.Json
import io.circe.syntax._
import org.http4s.implicits.path
import scala.collection.immutable.Seq
import scala.concurrent.duration.FiniteDuration

trait AuthorityService[F[_]] {
  def authorities: F[Seq[Authority]]
}

object AuthorityService:
  def apply[F[_]](using ev: AuthorityService[F]): AuthorityService[F] = ev

  def impl(fsaClient: FSAClient[IO, Json], authorityParser: AuthorityParser): AuthorityService[IO] =
    new AuthorityService[IO] {
      def authorities =
        return fsaClient
          .fetch(path"authorities" / "basic")
          .map(authorityParser.summariseAuthorites)
    }

  def withCache[F[_]: Async](
    fsaClient: AuthorityService[F],
    redisResource: Resource[F, Cache[F, String, Json]]
  ): AuthorityService[F] =
    new AuthorityService[F] {
      val KEY         = "authorities"
      def authorities =
        return redisResource.use(
          redis =>
            for {
              cachedData  <- redis.get(KEY)
              data         = cachedData.flatMap(_.as[Seq[Authority]].toOption)
              authorities <- data match
                               case Some(authorities) => Monad[F].pure(authorities)
                               case None              =>
                                 fsaClient.authorities.flatTap {
                                   authorities => redis.set(KEY, authorities.asJson)
                                 }
            } yield authorities
        )
    }
