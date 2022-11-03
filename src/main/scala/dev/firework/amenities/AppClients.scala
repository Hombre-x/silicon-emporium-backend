package dev.firework.amenities

import cats.effect.Concurrent
import dev.firework.http.client.algebras.BestBuyClient
import org.http4s.client.Client

trait AppClients[F[_]]:

  def bestBuyClient: BestBuyClient[F]

end AppClients

object AppClients:
  def make[F[_] : Concurrent](client: Client[F]): AppClients[F] = new AppClients[F]:
    override def bestBuyClient: BestBuyClient[F] =
      BestBuyClient.make(client)
