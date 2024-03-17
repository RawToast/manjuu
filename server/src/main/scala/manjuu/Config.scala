package manjuu

import com.comcast.ip4s._
import com.typesafe.config.ConfigFactory
import org.http4s.Uri

case class Config(
  serverHost: Ipv4Address,
  serverPort: Port,
  redisHost: String,
  resisPort: Int,
  apiUri: Uri
)

object Config:
  def load: Config =
    val conf = ConfigFactory.load()

    val redisHost = conf.getString("services.redis.host")
    val resisPort = conf.getInt("services.redis.port")

    val apiUriString = conf.getString("services.api.uri")
    val apiUri       = Uri
      .fromString(apiUriString)
      .getOrElse(throw new IllegalArgumentException("Invalid API URI"))

    val serverPortInt = conf.getInt("app.port")
    val serverPort    = Port
      .fromInt(serverPortInt)
      .getOrElse(throw new IllegalArgumentException("Server port not configured"))
    val serverHost    = ipv4"0.0.0.0"

    Config(serverHost, serverPort, redisHost, resisPort, apiUri)
