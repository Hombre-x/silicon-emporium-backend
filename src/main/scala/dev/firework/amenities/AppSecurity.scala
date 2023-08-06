package dev.firework.amenities

import cats.effect.MonadCancelThrow
import org.typelevel.log4cats.Logger

import dev.firework.algebras.postgres.Users
import dev.firework.algebras.auth.Auth
import dev.firework.domain.skunkTypes.Pool


abstract class AppSecurity[F[_]: MonadCancelThrow : Logger](postgres: Pool[F]):

  val users: Users[F] = Users.make(postgres)
  val auth: Auth[F] = Auth.make(users)

end AppSecurity


object AppSecurity:
  def make[F[_] : MonadCancelThrow : Logger](postgres: Pool[F]): AppSecurity[F] =
    new AppSecurity[F](postgres) {}
