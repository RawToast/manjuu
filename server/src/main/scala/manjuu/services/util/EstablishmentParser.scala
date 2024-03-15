package manjuu.services.util

import io.circe.Json
import io.circe.optics.JsonPath._
import io.circe.syntax._
import monocle.Traversal
import scala.collection.immutable.Seq

trait EstablishmentParser:
  def establishmentRating(singleEstablishment: Json): Option[String]

  def countEstablishmentRatings(validEstablishmentsJson: Json): Map[String, Int]

object EstablishmentParser:
  def impl(): EstablishmentParser = new EstablishmentParser:

    def establishmentRating(singleEstablishment: Json): Option[String] =
      root.RatingValue.string.getOption(singleEstablishment)

    def countEstablishmentRatings(validEstablishments: Json): Map[String, Int] =
      import cats.implicits._

      // AuthoritySummary(name: String, ratings: RatingSummary)
      val scores: Seq[String] =
        root.establishments.each.RatingValue.string.getAll(validEstablishments)

      // Fold and combine with an empty map to build totals for each score
      scores.foldLeft(Map.empty[String, Int])((scoreMap, score) => scoreMap |+| Map(score -> 1))
