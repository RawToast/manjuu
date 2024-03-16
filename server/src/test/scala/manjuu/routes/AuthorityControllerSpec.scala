package manjuu.routes

import manjuu.domain.{Authority, AuthoritySummary, EstablishmentRatings}
import manjuu.services.{AuthorityService, EstablishmentService, EstablishmentServiceError}
import manjuu.services.util.RatingsFormatter

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import io.circe._
import io.circe.Json
import io.circe.literal._
import io.circe.syntax._
import org.http4s._
import org.http4s.Method.GET
import org.http4s.circe._
import org.http4s.dsl._
import org.http4s.dsl.io._
import org.http4s.implicits._
import scala.collection.immutable.Seq

class RatingsFormatterSpec extends munit.FunSuite:
  import RatingsFormatterSpec._

  test("must return a list of authorities"):
    val authorityController =
      AuthorityController.impl(stubAuthorityService, stubEstablishmentService)
    val routes              = authorityController.routes.orNotFound
    val request             = Request[IO](GET, uri"/authority")

    val response: IO[Response[IO]] = routes.run(request)
    val actualResponse             = response.unsafeRunSync()

    val responseJson = actualResponse.as[Json].unsafeRunSync()

    assertEquals(actualResponse.status, Ok)
    assertEquals(
      responseJson,
      json"""[{"name":"Test Authority","id":1,"establishments":48}]"""
    )

  test("must return a summary of an authority"):
    val authorityController =
      AuthorityController.impl(stubAuthorityService, stubEstablishmentService)
    val routes              = authorityController.routes.orNotFound
    val request             = Request[IO](GET, uri"/authority/1")

    val response: IO[Response[IO]] = routes.run(request)
    val actualResponse             = response.unsafeRunSync()

    val responseJson = actualResponse.as[Json].unsafeRunSync()

    assertEquals(actualResponse.status, Ok)
    assertEquals(
      responseJson,
      json"""{
        "name":"Test Authority",
        "url":"http://test-authority.com",
        "establishments":48,
        "ratings": {
          "Standard" : 
            {
              "five":1,
              "four":2,
              "three":3,
              "two":4,
              "one":5,
              "zero":6,
              "exempt":7,
              "awaitingInspection":8
            }
          }
      }"""
    )

  test("must return the summary of an Scottish authority"):
    val authorityController        =
      AuthorityController.impl(stubAuthorityService, stubEstablishmentService)
    val routes                     = authorityController.routes.orNotFound
    val request                    = Request[IO](GET, uri"/authority/2")
    val response: IO[Response[IO]] = routes.run(request)
    val actualResponse             = response.unsafeRunSync()

    val responseJson = actualResponse.as[Json].unsafeRunSync()

    assertEquals(actualResponse.status, Ok)
    assertEquals(
      responseJson,
      json"""{
        "name":"Test Authority",
        "url":"http://test-authority.com",
        "establishments":48,
        "ratings": {
          "Scottish" : 
            {
              "passAndEatSafe":1,
              "pass":2,
              "improvementRequired":3,
              "awaitingPublication":4,
              "awaitingInspection":5,
              "exempt":6
            }
          }
      }"""
    )

  test("must return a 422 when the ratings are invalid"):
    val establishmentService = new EstablishmentService[IO]:
      def hygieneRatings(id: Int): IO[Either[EstablishmentServiceError, AuthoritySummary]] =
        IO(Left(EstablishmentServiceError.InvalidRatings("Invalid ratings")))

    val authorityController = AuthorityController.impl(stubAuthorityService, establishmentService)
    val routes              = authorityController.routes.orNotFound
    val request             = Request[IO](GET, uri"/authority/1")

    val response: Response[IO] = routes.run(request).unsafeRunSync()

    assertEquals(response.status, UnprocessableEntity)

  test("must return a 404 when the authority is not found"):
    val establishmentService = new EstablishmentService[IO]:
      def hygieneRatings(id: Int): IO[Either[EstablishmentServiceError, AuthoritySummary]] =
        IO(Left(EstablishmentServiceError.AuthorityNotFound))

    val authorityController = AuthorityController.impl(stubAuthorityService, establishmentService)
    val routes              = authorityController.routes.orNotFound
    val request             = Request[IO](GET, uri"/authority/1")

    val response: Response[IO] = routes.run(request).unsafeRunSync()

    assertEquals(response.status, NotFound)

object RatingsFormatterSpec:
  implicit val runtime: IORuntime = cats.effect.unsafe.IORuntime.global

  val stubAuthorityService = new AuthorityService[IO] {
    def authorities = IO(
      Seq(
        Authority(name = "Test Authority", id = 1, establishments = 48)
      )
    )
  }

  val stubEstablishmentService = new EstablishmentService[IO]:
    def hygieneRatings(id: Int): IO[Either[EstablishmentServiceError, AuthoritySummary]] =
      id match
        case 2 =>
          IO(
            Right(
              AuthoritySummary(
                name = "Test Authority",
                url = "http://test-authority.com",
                establishments = 48,
                ratings = EstablishmentRatings.Scottish(
                  passAndEatSafe = 1,
                  pass = 2,
                  improvementRequired = 3,
                  awaitingPublication = 4,
                  awaitingInspection = 5,
                  exempt = 6
                )
              )
            )
          )
        case 1 =>
          IO(
            Right(
              AuthoritySummary(
                name = "Test Authority",
                url = "http://test-authority.com",
                establishments = 48,
                ratings = EstablishmentRatings.Standard(
                  five = 1,
                  four = 2,
                  three = 3,
                  two = 4,
                  one = 5,
                  zero = 6,
                  exempt = 7,
                  awaitingInspection = 8
                )
              )
            )
          )
        case _ => IO(Left(EstablishmentServiceError.AuthorityNotFound))
