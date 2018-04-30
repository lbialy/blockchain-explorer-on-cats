package machinespir.it.blockchainexploreroncats

package object rpc {

  case class RpcCall(jsonrpc: String = "2.0", id: Long, method: String, params: List[String])

  case class RpcCallResult[A](id: Long, jsonrpc: String, result: Option[A])

  case class GetTransactionByHashResult(hash: String,
                                        nonce: String,
                                        blockHash: String,
                                        blockNumber: String,
                                        transactionIndex: String,
                                        from: String,
                                        to: String,
                                        value: String,
                                        gas: String,
                                        gasPrice: String,
                                        input: String)

  case class TransactionHash(value: String) extends AnyVal

}
