package dev.firework.http.routes

import cats.MonadThrow
import cats.syntax.all.*
import cats.effect.Concurrent

import org.typelevel.log4cats.Logger

import org.http4s.{EntityDecoder, HttpRoutes}
import org.http4s.circe.{JsonDecoder, jsonOf}
import org.http4s.circe.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

import io.circe.syntax.*

import dev.firework.algebras.postgres.Users
import dev.firework.algebras.auth.Auth
import dev.firework.domain.user.*
import dev.firework.instances.UserInstances.given


case class AuthRoutes[F[_] : MonadThrow : JsonDecoder : Concurrent : Logger](auth: Auth[F]) extends Http4sDsl[F]:

  private val prefixPath = "/auth"

  private given CreateUserEntityDecoder: EntityDecoder[F, CreateUser] = jsonOf[F, CreateUser]

  private given LoginUserEntityDecoder: EntityDecoder[F, LoginUser] = jsonOf[F, LoginUser]

  private def httpRoutes: HttpRoutes[F] =

    HttpRoutes.of[F] {
          
      case req @ POST -> Root / "register" =>
        req.as[CreateUser].flatMap( user => 
          auth
            .register(user)
            .flatMap(Created(_))
            .recoverWith {
              case UserInUse(_) => Conflict(s"${user.username}")
            }
        )

      case req @ POST -> Root / "login" =>
        req.as[LoginUser].flatMap { user =>
          auth
            .login(user.username, user.password)
            .flatMap(Ok(_))
            .recoverWith {
              case UserNotFound(_) | InvalidPassword(_) => Forbidden()
            }
        
        }
    }

  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)
