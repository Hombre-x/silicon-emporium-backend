package dev.firework.httpLayer

import cats.Monad
import cats.effect.Sync
import cats.syntax.all.*
import cats.Parallel

import org.typelevel.log4cats.Logger

import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.dsl.impl.*
import org.http4s.dsl.*
import eu.timepit.refined.types.string.NonEmptyString

import dev.firework.core.Search
import dev.firework.httpLayer.routes.SearchRoutes
object Controller:

  def helloWorldRoutes[F[_] : Monad]: HttpRoutes[F] =

    val dsl = new Http4sDsl[F] {}

    import dsl.*

    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name =>
        Ok(s"{hello : $name}")
    }
    
  end helloWorldRoutes
  
    
  def allRoutes[F[_] : Sync : Parallel : Logger]: HttpRoutes[F] =
    SearchRoutes[F].routes <+> helloWorldRoutes[F]
    
  def allRoutesApp[F[_] : Sync : Parallel : Logger]: HttpApp[F] =
    allRoutes.orNotFound
  
