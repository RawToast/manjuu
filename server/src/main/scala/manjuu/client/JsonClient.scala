package manjuu.client

import cats.effect.IO
import cats.effect.kernel.Resource
import io.circe.Json
import org.http4s._
import org.http4s.Status.Successful
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.dsl._
import org.log4s._

trait FSAClient[F[_], R]:
  def fetch(path: String): F[R]

object FSAClient:
  def json(client: Resource[IO, Client[IO]], baseUri: Uri) =
    new FSAClient[IO, Json]:
      def fetch(path: String) =
        print("fetching")
        return client.use(
          _.expect[Json](
            Request(
              Method.GET,
              baseUri / path,
              headers = Headers("x-api-version" -> "2")
            )
          )
        )
