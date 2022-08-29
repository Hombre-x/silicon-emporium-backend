package dev.firework.httpLayer

import cats._
import cats.effect._
import cats.implicits._
import org.http4s.circe._
import org.http4s._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.dsl._
import org.http4s.dsl.impl._
import org.http4s.headers._
import org.http4s.implicits._
import org.http4s.server._

object Routes:

  def helloWorldRoutes[F[_] : Monad]: HttpRoutes[F] =
    
    val dsl = new Http4sDsl[F]{}
    
    import dsl._
    
    HttpRoutes.of[F]{
        case GET -> Root / "hello" / name =>
          Ok(s"{hello : $name}")
      }
    
  end helloWorldRoutes

end Routes

