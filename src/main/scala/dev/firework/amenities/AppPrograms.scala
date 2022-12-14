package dev.firework.amenities

import cats.Parallel
import cats.effect.Sync
import dev.firework.core.Search
import dev.firework.amenities.AppClients
import org.http4s.client.Client

abstract class AppPrograms[F[_] : Sync : Parallel](private val clients: AppClients[F]):

  val search: Search[F] = Search(clients)

end AppPrograms

object AppPrograms:
  def make[F[_] : Sync : Parallel](clients: AppClients[F]): AppPrograms[F] =
    new AppPrograms[F](clients) {}