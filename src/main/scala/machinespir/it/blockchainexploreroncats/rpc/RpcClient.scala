package machinespir.it.blockchainexploreroncats.rpc

import cats.effect.Effect
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.Method.POST
import org.http4s.circe.{jsonOf, _}
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.{EntityDecoder, Uri}

case class RpcClient[F[_]: Effect](httpClient: Client[F],
                                   nodeHost: String,
                                   nodePort: Int,
                                   scheme: String = "http://") extends Http4sClientDsl[F] {

  private val Right(uri) = Uri.fromString(s"$scheme$nodeHost:$nodePort")

  implicit val getTransactionByHashResultDecoder: EntityDecoder[F, RpcCallResult[GetTransactionByHashResult]] =
    jsonOf[F, RpcCallResult[GetTransactionByHashResult]]

  def fetchTransaction(hash: TransactionHash, id: Long): F[RpcCallResult[GetTransactionByHashResult]] = {
    val request = POST(uri, RpcCall(id = id, method = "eth_getTransactionByHash", params = hash.value :: Nil).asJson)

    httpClient.expect[RpcCallResult[GetTransactionByHashResult]](request)
  }

}
