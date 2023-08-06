package dev.firework.amenities

import cats.effect.Concurrent

import org.http4s.client.Client

import dev.firework.http.client.algebras.BestBuyClient

trait AppClients[F[_]]:

  def bestBuyClient: BestBuyClient[F]

object AppClients:
  def make[F[_]: Concurrent](client: Client[F]): AppClients[F] =
    new AppClients[F]:
      override def bestBuyClient: BestBuyClient[F] =
        BestBuyClient.make(client)
