package manjuu.domain

import manjuu.Server.authParser

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

case class Authority(name: String, id: Int, establishments: Int)

object Authority:
  given Codec[Authority] = deriveCodec

enum EstablishmentRatings:
  case Standard(
    five: Int,
    four: Int,
    three: Int,
    two: Int,
    one: Int,
    zero: Int,
    exempt: Int,
    awaitingInspection: Int
  )
  case Scottish(
    passAndEatSafe: Int,
    pass: Int,
    improvementRequired: Int,
    awaitingPublication: Int,
    awaitingInspection: Int,
    exempt: Int
  )
  case Exempt(exempt: Int)

object EstablishmentRatings:
  given Codec[EstablishmentRatings] = deriveCodec

enum ErrorResponse:
  case NotFound(message: String)
  case BadRequest(message: String)
  case InternalServerError(message: String)

case class AuthoritySummary(
  name: String,
  url: String,
  establishments: Int,
  ratings: EstablishmentRatings
)

object AuthoritySummary:
  given Codec[AuthoritySummary] = deriveCodec
