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

  // def stub(): AuthorityService[IO] =
  //   new AuthorityService[IO] {
  //     def authorities = IO.pure(
  //       Seq(
  //         Authority(name = "Test Authority", id = 1, establishments = 48),
  //         Authority("Another Authority", 2, 123),
  //         Authority("Third Authority", 3, 663),
  //         Authority("Fourth Authority", 4, 6),
  //         Authority("Fifth and final authority", 5, 44),
  //         Authority("Town", 6, 58),
  //         Authority("Major City", 7, 9001),
  //         Authority("Village", 8, 1),
  //         Authority("Small City", 9, 345),
  //         Authority("Matlock", 10, 345)
  //       )
  //     )
  //   }

  def impl(fsaClient: FSAClient[IO, Json], authorityParser: AuthorityParser): AuthorityService[IO] =
    new AuthorityService[IO] {
      def authorities =
        return fsaClient
          .fetch("authorities/basic")
          .map(authorityParser.summariseAuthorites)
    }
