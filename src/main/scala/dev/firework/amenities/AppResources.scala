package dev.firework.amenities

import cats.Parallel
import cats.effect.{Async, Resource}

import org.http4s.client.Client

import dev.firework.amenities.MkHttpClient

case class AppResources[F[_]](client: Client[F])

object AppResources:
  def make[F[_] : Async]: Resource[F, AppResources[F]] =
    MkHttpClient.make[F].create.map(AppResources.apply)

