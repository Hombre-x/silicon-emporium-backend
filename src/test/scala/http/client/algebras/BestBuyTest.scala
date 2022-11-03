package http.client.algebras

import cats.effect.{IO, IOApp, Resource, Sync}
import cats.syntax.all.*
import cats.syntax.parallel.*
import dev.firework.domain.scrapper.ScrapperResult
import io.circe.{HCursor, Json}
import io.circe.syntax.*
import io.circe.parser.parse
import dev.firework.domain.search.{BestBuyItem, Item}
import dev.firework.instances.ItemInstances.given
import dev.firework.http.client.algebras.BestBuyClient
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.client.Client
import org.scalatest.funsuite.*

object BestBuyTest extends IOApp.Simple:
  
  val client = EmberClientBuilder.default[IO].build
  
  def response(client: Resource[IO, Client[IO]]): IO[ScrapperResult] =
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