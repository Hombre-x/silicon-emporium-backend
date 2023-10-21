package http.client.algebras

import cats.effect.{IO, IOApp, Resource}
import dev.firework.http.client.algebras.BestBuyClient
import dev.firework.domain.search.Item
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.client.Client

object BestBuyTest extends IOApp.Simple:
  
  val client = EmberClientBuilder.default[IO].build
  
  def response(client: Resource[IO, Client[IO]]): IO[Item] =
    client.use(
      cl =>
        BestBuyClient
          .make(cl)
          .getItem("Logitech MX Master 3s")
    )

  

  override def run: IO[Unit] =
    for
      res <- response(client)
      _ <- IO.println(res)
    yield ()
    
end BestBuyTest