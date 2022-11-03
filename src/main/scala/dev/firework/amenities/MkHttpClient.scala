package dev.firework.amenities

import cats.effect.{Async, Resource}
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder

trait MkHttpClient[F[_]]:

  def create: Resource[F, Client[F]]
  
end MkHttpClient


object MkHttpClient:
  
  def make[F[_] : Async]: MkHttpClient[F] = new MkHttpClient[F]:
    override def create: Resource[F, Client[F]] =
      EmberClientBuilder
        .default[F]
        .build

end MkHttpClient
