package dev.firework.amenities

import cats.effect.Async
import cats.effect.kernel.Resource
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import org.typelevel.log4cats.Logger

trait MkHttpServer[F[_]]:
  
  def create(app: HttpApp[F]): Resource[F, Server]

end MkHttpServer


object MkHttpServer:
  
  def make[F[_] : Async : Logger](config: HttpServerConfig): MkHttpServer[F] = new MkHttpServer[F]:
    override def create(app: HttpApp[F]): Resource[F, Server] = 
      EmberServerBuilder
        .default[F]
        .withHost(config.host)
        .withPort(config.port)
        .withHttpApp(app)
        .build
  
end MkHttpServer
