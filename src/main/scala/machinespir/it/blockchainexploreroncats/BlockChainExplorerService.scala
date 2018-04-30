package machinespir.it.blockchainexploreroncats

import cats.effect.Effect
import cats.implicits._
import fs2.async.Ref
import io.circe.Json
import machinespir.it.blockchainexploreroncats.rpc.{RpcClient, TransactionHash}
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._

class BlockChainExplorerService[F[_] : Effect] extends Http4sDsl[F] {

  def service(idGen: Ref[F, Long], rpcClient: RpcClient[F]): HttpService[F] = {
    HttpService[F] {
      case GET -> Root / "transaction" / hash =>
        for {
          nextId <- idGen.get
          _ <- idGen.modify(_ + 1)
          transaction <- rpcClient.fetchTransaction(TransactionHash(hash), nextId)
          response <- buildResponse(transaction)
        } yield response
    }
  }

  private def buildResponse(transaction: rpc.RpcCallResult[rpc.GetTransactionByHashResult]) = {
    transaction.result
      .map(v => Ok(v.asJson))
      .getOrElse {
        NotFound(Json.obj("error" -> Json.fromString("transaction not found")))
      }
  }

}
