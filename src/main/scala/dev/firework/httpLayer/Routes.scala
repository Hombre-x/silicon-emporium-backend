package dev.firework.httpLayer

import cats.Monad
import org.http4s.HttpRoutes
import org.http4s.dsl.impl._
import org.http4s.dsl._


object Routes:

  def helloWorldRoutes[F[_]: Monad]: HttpRoutes[F] =

    val dsl = new Http4sDsl[F] {}

    import dsl.*

    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name =>
        Ok(s"{hello : $name}")
    }
