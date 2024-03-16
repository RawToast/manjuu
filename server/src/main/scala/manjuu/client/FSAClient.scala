package manjuu.client

import cats.effect.IO
import cats.effect.kernel.Resource
import io.circe.Json
import org.http4s._
import org.http4s.Status.Successful
import org.http4s.Uri.Path
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.dsl._
import org.log4s._

trait FSAClient[F[_], T]:
  def get[T](path: Path)(using EntityDecoder[F, T]): F[Option[T]]
  def fetch(path: String, params: Map[String, String] = Map.empty): F[Json]

object FSAClient:
  def impl(client: Resource[IO, Client[IO]], baseUri: Uri) =
    new FSAClient[IO, Json]:
      val versionHeader                                                    = "x-api-version" -> "2"
      def fetch(basePath: String, params: Map[String, String] = Map.empty) =
        return client.use(
          _.expect[Json](
            Request(
              Method.GET,
              (baseUri / basePath).withQueryParams(params),
              headers = Headers(versionHeader)
            )
          )
        )
      def get[T](path: Path)(using EntityDecoder[IO, T])                   =
        return client.use(
          _.expectOption[T](
            Request(
              Method.GET,
              path.segments.foldLeft(baseUri)(_ / _),
              headers = Headers(headers = Headers(versionHeader))
            )
          )
        )
