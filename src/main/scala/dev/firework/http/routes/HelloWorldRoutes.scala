package dev.firework.http.routes

import cats.Monad

import io.circe.syntax.*

import org.http4s.HttpRoutes
import org.http4s.circe.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

class HelloWorldRoutes[F[_] : Monad] extends Http4sDsl[F]:
  
  private val prefixPath = "/hello"
  
  private def httpRoutes: HttpRoutes[F] =
    HttpRoutes.of[F]{
      case GET -> Root / name =>
        Ok(s"""{"hello" : $name}""".asJson)
    }
    
  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)

end HelloWorldRoutes