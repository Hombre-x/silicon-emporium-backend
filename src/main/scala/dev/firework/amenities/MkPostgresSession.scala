package dev.firework.amenities

import cats.effect.Temporal
import cats.effect.std.Console
import cats.syntax.all.*

import fs2.io.net.Network

import org.typelevel.log4cats.Logger

import skunk.*
import skunk.codec.text.*
import skunk.implicits.*

import dev.firework.domain.config.PostgreSQLConfig
import dev.firework.domain.skunkTypes.{Pool, SessionPool}

import natchez.Trace

trait MkPostgresSession[F[_]]:

  def create: SessionPool[F]

object MkPostgresSession:

  def make[F[_] : Temporal : Trace : Console : Network : Logger](
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
            database = "silicon_emporium_db_test",
            user = config.user,
            password = config.password.value.some,
            max = 10,
            ssl = SSL.None
          ).evalTap(checkPostgresConnection)

