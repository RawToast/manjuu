package manjuu.services.util

import manjuu.domain.EstablishmentRatings

enum FormatterError:
  case NoRatings
  case InvalidData(message: String)

trait RatingsFormatter:
  def summariseRatings(
    ratings: Map[String, Int]
  ): Either[FormatterError, EstablishmentRatings]

object RatingsFormatter:

  val standardRatings = Set("5", "4", "3", "2", "1", "0", "Exempt", "AwaitingInspection")
  val scottishRatings =
    Set(
      "Pass",
      "Improvement Required",
      "Awaiting Publication",
      "Awaiting Inspection",
      "Exempt",
      "Pass and Eat Safe"
    )

  def impl(): RatingsFormatter = new RatingsFormatter:
    import FormatterError._

    def summariseRatings(ratings: Map[String, Int]): Either[FormatterError, EstablishmentRatings] =
      import cats.implicits._
      return ratings match
        case r if r.isEmpty                          => Left(NoRatings)
        case r if r.keySet == Set("Exempt")          => Right(EstablishmentRatings.Exempt(r("Exempt")))
        case r if r.keySet.subsetOf(standardRatings) =>
          Right(
            EstablishmentRatings.Standard(
              five = ratings.getOrElse("5", 0),
              four = ratings.getOrElse("4", 0),
              three = ratings.getOrElse("3", 0),
              two = ratings.getOrElse("2", 0),
              one = ratings.getOrElse("1", 0),
              zero = ratings.getOrElse("0", 0),
              exempt = ratings.getOrElse("Exempt", 0),
              awaitingInspection = ratings.getOrElse("AwaitingInspection", 0)
            )
          )
        case r if r.keySet.subsetOf(scottishRatings) =>
          Right(
            EstablishmentRatings.Scottish(
              passAndEatSafe = ratings.getOrElse("Pass and Eat Safe", 0),
              pass = ratings.getOrElse("Pass", 0),
              improvementRequired = ratings.getOrElse("Improvement Required", 0),
              awaitingPublication = ratings.getOrElse("Awaiting Publication", 0),
              awaitingInspection = ratings.getOrElse("Awaiting Inspection", 0),
              exempt = ratings.getOrElse("Exempt", 0)
            )
          )
        case _                                       => Left(InvalidData("Invalid ratings data"))
