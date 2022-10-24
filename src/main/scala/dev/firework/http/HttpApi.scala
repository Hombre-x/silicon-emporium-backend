package dev.firework.http

import cats.Parallel
import cats.syntax.semigroupk.*
import cats.effect.Sync

import org.typelevel.log4cats.Logger
import org.http4s.{HttpRoutes, HttpApp}
import org.http4s.server.middleware.CORS

import dev.firework.amenities.AppPrograms
import dev.firework.http.routes.*

sealed abstract class HttpApi[F[_] : Sync : Parallel : Logger](
  private val programs: AppPrograms[F]
):
  
  private val searchRoutes = SearchRoutes[F](programs.search).routes
  
  private val helloWorldRoutes = HelloWorldRoutes[F].routes
  
  private val allRoutes = searchRoutes <+> helloWorldRoutes
  
  private def middleware(http: HttpRoutes[F]): HttpRoutes[F] =
    CORS.policy.withAllowOriginAll(http)
  
  val httpApp: HttpApp[F] = middleware(allRoutes).orNotFound
  
end HttpApi

object HttpApi:
  def make[F[_]: Sync : Parallel : Logger](
    programs: AppPrograms[F]
  ): HttpApi[F] = new HttpApi[F](programs) {}