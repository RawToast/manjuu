package manjuu.services.util

import manjuu.Responses.multiAuthorityResponse
import manjuu.domain.Authority

import io.circe.literal.json

class AuthorityParserSpec extends munit.FunSuite:

  val jsonWithDifferentStructure = json"""{"message": "hello"}"""
  val parser                     = AuthorityParser.impl()

  test("must return an empty list when given invalid input"):
    val authorites: Seq[Authority] = parser.summariseAuthorites(jsonWithDifferentStructure)

    assert(authorites.isEmpty)

  test("must return a list of authorities when given valid input"):
    val authorites: Seq[Authority] = parser.summariseAuthorites(multiAuthorityResponse)
    assert(authorites.nonEmpty)
    assert(authorites.size == 5)

    val sorted = authorites.sortBy(_.name)

    assert(sorted.head.name == "Aberdeen City")
    assert(sorted.head.id == 197)
    assert(sorted.head.establishments == 1761)

    assert(sorted.last.name == "Amber Valley")
    assert(sorted.last.id == 48)
    assert(sorted.last.establishments == 967)


  test("must include the authority details"):
    val authorites: Seq[Authority] = parser.summariseAuthorites(singleAuthorityResponse)
    assert(authorites.size == 1)
    assert(authorites.head.name == "Aberdeen City")
    assert(authorites.head.id == 197)
    assert(authorites.head.establishments == 1761)


  val singleAuthorityResponse =
    json"""{
  "authorities": [
    {
      "LocalAuthorityId": 197,
      "LocalAuthorityIdCode": "760",
      "Name": "Aberdeen City",
      "EstablishmentCount": 1761,
      "SchemeType": 2,
      "links": [
        {
          "rel": "self",
          "href": "http://api.ratings.food.gov.uk/authorities/197"
        }
      ]
    }]}"""
