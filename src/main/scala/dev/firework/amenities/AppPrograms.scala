package dev.firework.amenities

import cats.Parallel
import cats.effect.Async
import org.typelevel.log4cats.Logger

import dev.firework.core.Search

abstract class AppPrograms[F[_] : Async : Parallel: Logger](private val clients: AppClients[F]):

  val search: Search[F] = Search(clients)

end AppPrograms

object AppPrograms:
  def make[F[_] : Async : Parallel : Logger](clients: AppClients[F]): AppPrograms[F] =
    new AppPrograms[F](clients) {}