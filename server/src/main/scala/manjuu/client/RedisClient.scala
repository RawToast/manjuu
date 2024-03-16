package manjuu.client

import cats.effect.kernel.{Async, Resource}
import dev.profunktor.redis4cats.Redis
import dev.profunktor.redis4cats.codecs.Codecs
import dev.profunktor.redis4cats.codecs.splits.SplitEpi
import dev.profunktor.redis4cats.data.RedisCodec
import dev.profunktor.redis4cats.effect.Log.Stdout.given
import io.circe.{Json, JsonObject}
import io.circe.parser.{decode => jsonDecode}
import io.circe.syntax._
import org.http4s.headers.`Access-Control-Max-Age`.Cache
import scala.concurrent.duration.FiniteDuration

trait Cache[F[_], K, V]:
  def get(key: K): F[Option[V]]
  def set(key: K, value: V): F[Unit]

object Cache:
  val eventSplitEpi: SplitEpi[String, Json] =
    SplitEpi[String, Json](
      str => jsonDecode(str).getOrElse(JsonObject.empty.asJson),
      _.asJson.noSpaces
    )
  val jsonCodec                             = Codecs.derive(RedisCodec.Utf8, eventSplitEpi)

  def redis[F[_]: Async](uri: String, ttl: FiniteDuration): Resource[F, Cache[F, String, Json]] =
    Redis[F].simple(uri, jsonCodec).map {
      redis =>
        new Cache[F, String, Json]:
          def get(key: String): F[Option[Json]]      = redis.get(key)
          def set(key: String, value: Json): F[Unit] = redis.setEx(key, value, ttl)
    }
