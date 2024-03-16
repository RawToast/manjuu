package manjuu.services.util

import manjuu.domain.EstablishmentRatings

class RatingsFormatterSpec extends munit.FunSuite:

  val ratingsFormatter = RatingsFormatter.impl()
  test("must return an empty ratings error when given an empty map"):
    val ratingsSummary = ratingsFormatter.summariseRatings(Map.empty)
    assert(ratingsSummary == Left(FormatterError.NoRatings))

  test("must return standard ratings when provided with a single standard rating"):
    val ratingsSummary  = ratingsFormatter.summariseRatings(Map("5" -> 1))
    val expectedRatings = EstablishmentRatings.Standard(1, 0, 0, 0, 0, 0, 0, 0)
    assert(ratingsSummary == Right(expectedRatings))

  test("must return scottish ratings when provided with a map containing scottish ratings"):
    val ratingsSummary  = ratingsFormatter.summariseRatings(
      Map(
        "Pass and Eat Safe"    -> 1,
        "Pass"                 -> 2,
        "Improvement Required" -> 3,
        "Awaiting Publication" -> 4,
        "Awaiting Inspection"  -> 5,
        "Exempt"               -> 6
      )
    )
    val expectedRatings = EstablishmentRatings.Scottish(1, 2, 3, 4, 5, 6)
    assert(ratingsSummary == Right(expectedRatings))

  test("must return only exempt category when provided with a only exempt ratings"):
    val ratingsSummary  = ratingsFormatter.summariseRatings(Map("Exempt" -> 9001))
    val expectedRatings = EstablishmentRatings.Exempt(9001)
    assert(ratingsSummary == Right(expectedRatings))

  test("must return an invalid data error when provided with an invalid ratings map"):
    val ratingsSummary = ratingsFormatter.summariseRatings(Map("Pass" -> 1, "5" -> 1))
    assert(ratingsSummary == Left(FormatterError.InvalidData("Invalid ratings data")))
