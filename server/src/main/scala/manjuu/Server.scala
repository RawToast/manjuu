package manjuu

import manjuu.client.FSAClient
import manjuu.domain.EstablishmentRatings
import manjuu.routes.AuthorityController
import manjuu.services.{AuthorityService, EstablishmentService}
import manjuu.services.util.AuthorityParser

import cats.effect._
import com.comcast.ip4s._
import org.http4s.Method
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server._
import org.http4s.implicits.uri
import org.http4s.server.middleware.CORS
import org.http4s.server.middleware.CORSConfig

object Server extends IOApp:

  // val authorityService: AuthorityService[IO]         = AuthorityService.stub()
  val establishmentService: EstablishmentService[IO] = EstablishmentService.stub()

  val clientResource = EmberClientBuilder
    .default[IO]
    .build

  val authParser          = AuthorityParser.impl()
  val fsaClient           = FSAClient.json(clientResource, uri"http://api.ratings.food.gov.uk")
  val authorityService    = AuthorityService.impl(fsaClient, authParser)
  val authorityController = AuthorityController.impl[IO](authorityService, establishmentService)
  val authorityHttpApp    = authorityController.routes.orNotFound
  val corsEnabledApp      =
    CORS.policy.withAllowOriginAll.withAllowMethodsIn(Set(Method.GET)).apply(authorityHttpApp)

  def run(args: List[String]) =
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(corsEnabledApp)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
