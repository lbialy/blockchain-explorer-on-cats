package machinespir.it.blockchainexploreroncats

import cats.effect.IO

import scala.util.Try

case class Config(nodeHost: String, nodePort: Int, scheme: String = "http://")

object Config {

  case class ConfigurationError(message: String) extends RuntimeException(message)

  class ConfigurationProps(val props: Map[String, String]) {
    def getString(key: String): Either[ConfigurationError, String] =
      (props get key) toRight ConfigurationError(s"Key $key is missing!")

    def getInt(key: String): Either[ConfigurationError, Int] = for {
      stringValue <- getString(key)
      result <- Try(stringValue.toInt).toEither.left.map(_ => ConfigurationError(s"Key $key has invalid type!"))
    } yield result
  }

  lazy val props: ConfigurationProps = new ConfigurationProps(sys.env)

  def loadConfiguration(props: ConfigurationProps): Either[ConfigurationError, Config] = for {
    nodeHost <- props getString "nodeHost"
    nodePort <- props getInt "nodePort"
    scheme <- props getString "scheme"
  } yield Config(nodeHost, nodePort, scheme)

  lazy val load: IO[Config] = IO.fromEither(loadConfiguration(props))
}