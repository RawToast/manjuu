package manjuu.services

import manjuu.Responses._
import manjuu.client.Cache
import manjuu.client.FSAClient
import manjuu.services.util._

import cats.effect.IO
import cats.effect.kernel.Resource
import cats.effect.unsafe.IORuntime
import io.circe.Json
import org.http4s._
import org.http4s.Method.GET
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.implicits.uri

class EstablishmentServiceSpec extends munit.FunSuite:
  test("EstablishmentService can fetch hygiene ratings for an establishment"):
    val parser                      = EstablishmentParser.impl()
    val formatter                   = RatingsFormatter.impl()
    implicit val runtime: IORuntime = cats.effect.unsafe.IORuntime.global

    val client                                   = Client.fromHttpApp(EstablishmentServiceSpec.httpApp)
    val clientResource: Resource[IO, Client[IO]] = Resource.pure[IO, Client[IO]](client)
    val fsaClient                                = FSAClient.impl(clientResource, uri"")

    val service   = EstablishmentService.impl(fsaClient, parser, formatter)
    val withCache = EstablishmentService.withCache(service, Cache.memory[IO])
    val result    = withCache.hygieneRatings(111).unsafeRunSync()
    assert(result.isRight)

    val result2 = withCache.hygieneRatings(111).unsafeRunSync()
    assert(result2.isRight)

    assert(result == result2)

object EstablishmentServiceSpec:
  val dsl = new Http4sDsl[IO] {}
  import dsl._

  object LocalAuthorityIdQueryParam extends QueryParamDecoderMatcher[String]("localAuthorityId")
  object PageSizeQueryParam extends QueryParamDecoderMatcher[Int]("pageSize")
  val routes               = HttpRoutes.of[IO] {
    case GET -> Root / "establishments" :? LocalAuthorityIdQueryParam(
          localAuthorityId
        ) +& PageSizeQueryParam(
          pageSize
        ) =>
      Ok(validEstablishmentsJson)

    case GET -> Root / "authorities" / IntVar(id) =>
      authorityResponses.get(id) match
        case Some(authority) => Ok(authority)
        case None            => NotFound()
  }
  val httpApp: HttpApp[IO] = routes.orNotFound
