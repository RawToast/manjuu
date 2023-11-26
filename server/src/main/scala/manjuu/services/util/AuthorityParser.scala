package manjuu.services.util

import manjuu.domain.Authority

import io.circe.Json
import io.circe.optics.JsonPath._
import io.circe.syntax._
import monocle.Traversal

trait AuthorityParser:
  def summariseAuthorites(authData: Json): Seq[Authority]

object AuthorityParser:
  def impl(): AuthorityParser = new AuthorityParser:
    def summariseAuthorites(authData: Json): Seq[Authority] =
      // Use Json Optics as the Json model is large, plus we are only interested in 3 fields
      val traverseToAuthorities: Traversal[Json, Json] = root.authorities.each.json
      val authName                                     = root.Name.string
      val authId                                       = root.LocalAuthorityId.int
      val establishmentCount                           = root.EstablishmentCount.int

      return traverseToAuthorities
        .getAll(authData)
        .flatMap(
          json =>
            for {
              // Note that if any of these paths results in a `None` for an authority,
              // then that authority is filtered from the results.
              name  <- authName.getOption(json)
              id    <- authId.getOption(json)
              count <- establishmentCount.getOption(json)
            } yield Authority(name, id, count)
        )

// object JsonAuthorityParser extends AuthorityParser[Json] {
//   override def summariseAuthorites(authData: Json): Seq[Authority] = {

//     val authName: Optional[Json, String] = root.Name.string
//     val authId: Optional[Json, Int] = root.LocalAuthorityId.int
//     val establishmentCount: Optional[Json, Int] = root.EstablishmentCount.int

//     traverseToAuthorities
//       .getAll(authData)
//       .flatMap(json =>
//         for {
//           // Note that if any of these paths results in a `None` for an authority,
//           // then that authority is filtered from the results.
//           name <- authName.getOption(json)
//           id <- authId.getOption(json)
//           count <- establishmentCount.getOption(json)
//         } yield Authority(name, id, count))
//   }
// }
