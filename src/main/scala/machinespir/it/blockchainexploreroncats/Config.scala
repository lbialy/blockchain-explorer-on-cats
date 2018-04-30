package machinespir.it.blockchainexploreroncats

import cats.effect.IO
import pureconfig.error.ConfigReaderFailures

case class Config(nodeHost: String, nodePort: Int, scheme: String = "http://")

object Config {

  case class ConfigurationError(errors: ConfigReaderFailures) extends RuntimeException()

  lazy val load: IO[Config] = IO.fromEither(pureconfig.loadConfig[Config].left.map(ConfigurationError))
}