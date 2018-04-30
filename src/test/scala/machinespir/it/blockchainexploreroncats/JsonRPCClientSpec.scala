package machinespir.it.blockchainexploreroncats

import cats.effect.IO
import org.http4s.client.Client
import org.http4s.client.blaze.Http1Client

/**
  * This spec only works with a working and synced GEth node running on the same host
  */
class JsonRPCClientSpec extends org.specs2.mutable.Specification {

  import rpc._

  val testTransactionHash = "0x59ae735c0291ec79460968d01dbd5b4adf007571bfaa308b8ab1005bb5e3f500"

  implicit val http1Client: Client[IO] = Http1Client[IO]().unsafeRunSync()

  val rpcClient = RpcClient(http1Client, "0.0.0.0", 8545)

  "RpcClient should" >> {
    val transactionHash = TransactionHash(testTransactionHash)

    "look correct values up from blockchain node" >> {
      val rpcCallResult = rpcClient.fetchTransaction(transactionHash, 1).unsafeRunSync()

      rpcCallResult.id shouldEqual 1L

      val Some(result) = rpcCallResult.result
      result.hash shouldEqual testTransactionHash
      result.blockNumber shouldEqual "0x4e7b69"
    }
  }
}
