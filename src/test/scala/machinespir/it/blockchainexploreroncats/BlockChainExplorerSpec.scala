package machinespir.it.blockchainexploreroncats

import cats.effect.IO
import fs2.async.Ref
import machinespir.it.blockchainexploreroncats.rpc.RpcClient
import org.http4s._
import org.http4s.client.Client
import org.http4s.client.blaze.Http1Client
import org.http4s.implicits._
import org.specs2.matcher.MatchResult

class BlockChainExplorerSpec extends org.specs2.mutable.Specification {

  "Transactions API" >> {
    "return 200" >> {
      uriReturns200()
    }
    "return valid transaction details" >> {
      uriReturnsTransactionDetails()
    }
  }

  private[this] implicit val http1Client: Client[IO] = Http1Client[IO]().unsafeRunSync()

  private[this] val retTransaction: Response[IO] = {
    val uri = Uri.uri("/transaction/0x59ae735c0291ec79460968d01dbd5b4adf007571bfaa308b8ab1005bb5e3f500")
    val getHW = Request[IO](Method.GET, uri)
    new BlockChainExplorerService[IO].service(
      Ref[IO, Long](0).unsafeRunSync(),
      RpcClient[IO](http1Client,"0.0.0.0", 8545)
    ).orNotFound(getHW).unsafeRunSync()
  }

  private[this] def expectedTransactionDetails =
    """{"hash":"0x59ae735c0291ec79460968d01dbd5b4adf007571bfaa308b8ab1005bb5e3f500","nonce":"0x113","blockHash":"0xe75af90db11e459e7b017a59154caec041da6a51ca159ea85b92d3003474f1d8","blockNumber":"0x4e7b69","transactionIndex":"0x7","from":"0xd371519e9789a5bbd84faf18928c80ab243f48c8","to":"0x811ea13b60693d22b14139284db003af717fb8a4","value":"0x29a2241af62c0000","gas":"0x1d8a8","gasPrice":"0x2cb417800","input":"0x"}"""

  private[this] def uriReturns200(): MatchResult[Status] =
    retTransaction.status must beEqualTo(Status.Ok)

  private[this] def uriReturnsTransactionDetails(): MatchResult[String] =
    retTransaction.as[String].unsafeRunSync() must beEqualTo(expectedTransactionDetails)
}
