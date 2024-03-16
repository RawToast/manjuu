package manjuu.services

import manjuu.client.FSAClient
import manjuu.domain.Authority
import manjuu.services.util.AuthorityParser

import cats.effect.IO
import io.circe.Json
import scala.collection.immutable.Seq

trait AuthorityService[F[_]] {
  def authorities: F[Seq[Authority]]
}

object AuthorityService:
  def apply[F[_]](using ev: AuthorityService[F]): AuthorityService[F] = ev

  def impl(fsaClient: FSAClient[IO, Json], authorityParser: AuthorityParser): AuthorityService[IO] =
    new AuthorityService[IO] {
      def authorities =
        return fsaClient
          .fetch("authorities/basic")
          .map(authorityParser.summariseAuthorites)
    }
