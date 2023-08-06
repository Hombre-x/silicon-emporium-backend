package dev.firework.http.routes

import cats.MonadThrow
import cats.syntax.all.*
import cats.effect.Concurrent
import org.typelevel.log4cats.Logger

import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.circe.*
import org.http4s.server.Router
import org.http4s.EntityDecoder

import io.circe.syntax.*

import dev.firework.algebras.postgres.Users
import dev.firework.domain.user.CreateUser
import dev.firework.instances.UserInstances.given


case class UserRoutes[F[_] : MonadThrow : JsonDecoder : Concurrent : Logger](users: Users[F]) extends Http4sDsl[F]:
  
  private val prefixPath = "/user"
  
  private given CreateUserEntityDecoder: EntityDecoder[F, CreateUser] = jsonOf[F, CreateUser]
  
  private def httpRoutes: HttpRoutes[F] =

    HttpRoutes.of[F]:
      case req @ POST -> Root / "create" =>
        req.as[CreateUser].flatMap(
          user =>
            users.find(user.username).flatMap:
              case Some(userFound) => Conflict(userFound.username.asJson)
              case None => users.create(user) >> Created(user.username.asJson)
          )
    
    
  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)
  
end UserRoutes


