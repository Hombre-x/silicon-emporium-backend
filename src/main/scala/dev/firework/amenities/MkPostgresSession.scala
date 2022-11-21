package dev.firework.amenities

import cats.effect.std.Console
import cats.effect.{Concurrent, Resource}
import cats.syntax.flatMap.*
import cats.syntax.option.*

import fs2.io.net.Network

import natchez.Trace

import org.typelevel.log4cats.Logger

import skunk.*
import skunk.codec.text.*
import skunk.implicits.*

import dev.firework.domain.config.PostgreSQLConfig
import dev.firework.domain.skunkTypes.{Pool, SessionPool}

trait MkPostgresSession[F[_]]:

  def create: SessionPool[F]

object MkPostgresSession:

  def make[F[_]: Concurrent: Trace: Console: Network: Logger](
      config: PostgreSQLConfig
  ): MkPostgresSession[F] =
    
    new MkPostgresSession[F]:

      private def checkPostgresConnection(postgres: Pool[F]): F[Unit] =
        postgres.use(se =>
          se
            .unique(sql"select version();".query(text))
            .flatMap(v => Logger[F].info(s"Connected to PostgreSQL version: $v"))
        )

      override def create: SessionPool[F] =
        Session
          .pooled(
            host = config.host,
            port = config.port,
            database = "silicon_emporium_db",
            user = config.user,
            password = config.password.value.some,
            max = 1,
            ssl = SSL.System
          ).evalTap(checkPostgresConnection)

