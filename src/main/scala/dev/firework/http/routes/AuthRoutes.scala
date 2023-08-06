package dev.firework.http.routes

import cats.MonadThrow
import cats.effect.Concurrent
import cats.syntax.all.*

import org.typelevel.log4cats.Logger

import org.http4s.{EntityDecoder, HttpRoutes}
import org.http4s.circe.{jsonOf, JsonDecoder, _}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

import io.circe.syntax.*

import dev.firework.algebras.auth.Auth
import dev.firework.domain.user.*
import dev.firework.instances.UserInstances.given


case class AuthRoutes[F[_] : MonadThrow : JsonDecoder : Concurrent : Logger](auth: Auth[F]) extends Http4sDsl[F]:

  private val prefixPath = "/auth"

  private given CreateUserEntityDecoder: EntityDecoder[F, CreateUser] = jsonOf[F, CreateUser]

  private given LoginUserEntityDecoder: EntityDecoder[F, LoginUser] = jsonOf[F, LoginUser]
  
  private given ChangePassEntityDecoder: EntityDecoder[F, ChangePassUser] = jsonOf[F, ChangePassUser]

  private def httpRoutes: HttpRoutes[F] =

    HttpRoutes.of[F] {
          
      case req @ POST -> Root / "register" =>
        req.as[CreateUser].flatMap( user => 
          auth
            .register(user)
            .flatMap(username => Logger[F].debug("Imminent register incoming") >> Created(UserName(username).asJson))
            .recoverWith {
              case UserInUse(_) => Conflict(s"${user.username}")
            }
        )

      case req @ POST -> Root / "login" =>
        req.as[LoginUser].flatMap { user =>
          auth
            .login(user.username, user.password)
            .flatMap(Logger[F].debug("Imminent login request") >> Ok(_))
            .recoverWith {
              case UserNotFound(_) | InvalidPassword(_) =>
                Logger[F].debug("User not found or wrong password...") >> Forbidden()
            }
        }

      case req @ PUT -> Root / "reset" =>
        req.as[ChangePassUser].flatMap( user =>
          auth
            .changePass(user)
            .flatMap( username =>
              Logger[F].info(s"Changed password for ${user.username}") >>
                Accepted(UserName(username).asJson)
            )
            .recoverWith{
              case UserNotFound(_) => Forbidden("Can't change password.")
            }
        )
        
    }

  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)
