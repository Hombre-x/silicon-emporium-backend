package dev.firework.amenities

import cats.syntax.parallel.*
import cats.effect.{Async, Resource}
import cats.effect.std.Console

import org.typelevel.log4cats.Logger

import org.http4s.client.Client

import fs2.io.net.Network

import natchez.Trace

import dev.firework.domain.config.*
import dev.firework.domain.skunkTypes.Pool


case class AppResources[F[_]](client: Client[F], postgres: Pool[F])

object AppResources:
  def make[F[_] : Async : Trace : Console : Network : Logger](config: AppConfig): Resource[F, AppResources[F]] =
    (
      MkHttpClient.make[F].create,
      MkPostgresSession.make[F](config.postgreSQL).create
    ).parMapN(AppResources.apply)

