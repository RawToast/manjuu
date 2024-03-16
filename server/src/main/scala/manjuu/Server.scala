package manjuu

import manjuu.client.FSAClient
import manjuu.domain.EstablishmentRatings
import manjuu.routes.AuthorityController
import manjuu.services.{AuthorityService, EstablishmentService}
import manjuu.services.util.AuthorityParser
import manjuu.services.util.EstablishmentParser

import cats.effect._
import com.comcast.ip4s._
import org.http4s.Method
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server._
import org.http4s.implicits.uri
import org.http4s.server.middleware.CORS
import org.http4s.server.middleware.CORSConfig
import org.http4s.server.middleware.ErrorAction
import org.http4s.server.middleware.ErrorHandling

object Server extends IOApp:
  override protected def blockedThreadDetectionEnabled = true

  val clientResource = EmberClientBuilder
    .default[IO]
    .build

  val authParser                                     = AuthorityParser.impl()
  val establishmentParser                            = EstablishmentParser.impl()
  val fsaClient                                      = FSAClient.impl(clientResource, uri"https://api.ratings.food.gov.uk")
  val authorityService                               = AuthorityService.impl(fsaClient, authParser)
  val establishmentService: EstablishmentService[IO] =
    EstablishmentService.impl(fsaClient, establishmentParser)
  val authorityController                            = AuthorityController.impl[IO](authorityService, establishmentService)
  val authorityHttpApp                               = authorityController.routes.orNotFound
  val corsEnabledApp                                 =
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
