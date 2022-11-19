package dev.firework.http

import cats.Parallel
import cats.syntax.semigroupk.*
import cats.effect.{Async, Sync}

import org.typelevel.log4cats.Logger
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.server.middleware.CORS

import dev.firework.amenities.{AppPrograms, AppSecurity}
import dev.firework.http.routes.*

sealed abstract class HttpApi[F[_] : Async : Parallel : Logger](
  private val programs: AppPrograms[F],
  private val security: AppSecurity[F]
):
  
  private val searchRoutes = SearchRoutes[F](programs.search).routes
  
  private val helloWorldRoutes = HelloWorldRoutes[F].routes

  private val userRoutes = UserRoutes[F](security.users).routes
  
  private val authRoutes = AuthRoutes[F](security.auth).routes
  
  private val allRoutes =
    searchRoutes <+> helloWorldRoutes <+> userRoutes <+> authRoutes
  
  private def middleware(http: HttpRoutes[F]): HttpRoutes[F] =
    CORS.policy.withAllowOriginAll(http)
  
  val httpApp: HttpApp[F] = middleware(allRoutes).orNotFound
  
end HttpApi

object HttpApi:
  def make[F[_]: Async : Parallel : Logger](
    programs: AppPrograms[F],
    security: AppSecurity[F]
  ): HttpApi[F] = new HttpApi[F](programs, security) {}