package manjuu

import manjuu.client.Cache
import manjuu.client.FSAClient
import manjuu.domain.{Authority, EstablishmentRatings}
import manjuu.routes.AuthorityController
import manjuu.services.{AuthorityService, EstablishmentService}
import manjuu.services.util.{AuthorityParser, EstablishmentParser, RatingsFormatter}

import cats.effect._
import com.comcast.ip4s._
import dev.profunktor.redis4cats.Redis
import dev.profunktor.redis4cats.codecs.Codecs
import dev.profunktor.redis4cats.codecs.splits.SplitEpi
import dev.profunktor.redis4cats.data.RedisCodec
import org.http4s.Method
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server._
import org.http4s.implicits.uri
import org.http4s.server.middleware.{CORS, CORSConfig, ErrorAction, ErrorHandling}
import scala.concurrent.duration.DurationInt

object Server extends IOApp:
  override protected def blockedThreadDetectionEnabled = true

  val clientResource = EmberClientBuilder
    .default[IO]
    .build

  val authParser          = AuthorityParser.impl()
  val establishmentParser = EstablishmentParser.impl()
  val redisUri            = "redis://localhost:6379"

  val redisResource = Cache.redis[IO](redisUri, 1.hour)

  val fsaClient                                      = FSAClient.impl(clientResource, uri"https://api.ratings.food.gov.uk")
  val authorityService                               = AuthorityService.impl(fsaClient, authParser)
  val ratingsFormatter                               = RatingsFormatter.impl()
  val establishmentService: EstablishmentService[IO] =
    EstablishmentService.impl(fsaClient, establishmentParser, ratingsFormatter)

  val cachedEstablishmentService = EstablishmentService.withCache(
    establishmentService,
    redisResource
  )
  val cachedAuthorityService     = AuthorityService.withCache(authorityService, redisResource)

  val authorityController =
    AuthorityController.impl[IO](cachedAuthorityService, cachedEstablishmentService)
  val authorityHttpApp    = authorityController.routes.orNotFound
  val corsEnabledApp      =
    CORS.policy.withAllowOriginAll.withAllowMethodsIn(Set(Method.GET)).apply(authorityHttpApp)

  val corsEnabledAppWithErrorLogging = ErrorHandling.Recover.total(
    ErrorAction.log(
      corsEnabledApp,
      messageFailureLogAction = (t, msg) =>
        IO.println(msg) >>
          IO.println(t),
      serviceErrorLogAction = (t, msg) =>
        IO.println(msg) >>
          IO.println(t)
    )
  )
  def run(args: List[String])        =
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(corsEnabledAppWithErrorLogging)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
