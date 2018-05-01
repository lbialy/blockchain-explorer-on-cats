package machinespir.it.blockchainexploreroncats

import cats.effect.{Effect, IO}
import fs2.{Stream, StreamApp}
import fs2.async.Ref
import machinespir.it.blockchainexploreroncats.rpc.RpcClient
import org.http4s.HttpService
import org.http4s.client.blaze.Http1Client
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.ExecutionContext

object BlockChainExplorerServer extends StreamApp[IO] {

  import scala.concurrent.ExecutionContext.Implicits.global

  def configIO: Stream[IO, Config] = Stream.eval(Config.load)

  def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, StreamApp.ExitCode] = {
    ServerStream.stream[IO](configIO)
  }

}

object ServerStream {

  private def http1ClientStream[F[_] : Effect] = Http1Client.stream[F]()

  private def idGenStream[F[_] : Effect] = Stream.eval(Ref[F, Long](0))

  def blockChainExplorerService[F[_] : Effect](rpcClient: RpcClient[F], idGen: Ref[F, Long]): HttpService[F] = {
    new BlockChainExplorerService[F].service(idGen, rpcClient)
  }

  def stream[F[_] : Effect](configStream: Stream[F, Config])(implicit ec: ExecutionContext): Stream[F, StreamApp.ExitCode] = {
    for {
      config <- configStream
      httpClient <- http1ClientStream
      idGen <- idGenStream
      rpcClient = RpcClient(httpClient, config.nodeHost, config.nodePort, config.scheme)
      serverStream <-
        BlazeBuilder[F]
          .bindHttp(8080, "0.0.0.0")
          .mountService(blockChainExplorerService(rpcClient, idGen), "/")
          .serve
    } yield serverStream
  }

}
