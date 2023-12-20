package manjuu.domain

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

case class Authority(name: String, id: Int, establishments: Int)

object Authority:
  given Codec[Authority] = deriveCodec

enum EstablishmentRatings:
  case Standard(five: Int, four: Int, three: Int, two: Int, one: Int, zero: Int, exempt: Int)
  case Scottish(pass: Int, improvementRequired: Int, exempt: Int)

object EstablishmentRatings:
  given Codec[EstablishmentRatings] = deriveCodec

enum RatingSummary:
  case Standard(
    five: Int,
    four: Int,
    three: Int,
    two: Int,
    one: Int,
    zero: Int,
    exempt: Int
  )
  case Scottish(pass: Int, improvementRequired: Int, exempt: Int)

object RatingSummary:
  given Codec[RatingSummary] = deriveCodec

  def fromMap(ratingsMap: Map[String, Int]) =
    val isStandard =
      Seq("5", "4", "3", "2", "1", "0").exists(ratingsMap.keySet.contains(_))
    val isScottish =
      Seq("Pass", "Improvement Required").exists(ratingsMap.keySet.contains(_))

    if isStandard then
      Standard(
        five = ratingsMap.getOrElse("5", 0),
        four = ratingsMap.getOrElse("4", 0),
        three = ratingsMap.getOrElse("3", 0),
        two = ratingsMap.getOrElse("2", 0),
        one = ratingsMap.getOrElse("1", 0),
        zero = ratingsMap.getOrElse("0", 0),
        exempt = ratingsMap.getOrElse("Exempt", 0)
      )
    else if isScottish then
      Scottish(
        pass = ratingsMap.getOrElse("Pass", 0),
        improvementRequired = ratingsMap.getOrElse("Improvement Required", 0),
        exempt = ratingsMap.getOrElse("Exempt", 0)
      )
    else // Assume standard for now
      Standard(
        five = ratingsMap.getOrElse("5-star", 0),
        four = ratingsMap.getOrElse("4-star", 0),
        three = ratingsMap.getOrElse("3-star", 0),
        two = ratingsMap.getOrElse("2-star", 0),
        one = ratingsMap.getOrElse("1-star", 0),
        zero = ratingsMap.getOrElse("0-star", 0),
        exempt = ratingsMap.getOrElse("Exempt", 0)
      )

case class AuthoritySummary(name: String, url: String, establishments: Int, ratings: RatingSummary)

object AuthoritySummary:
  given Codec[AuthoritySummary] = deriveCodec
