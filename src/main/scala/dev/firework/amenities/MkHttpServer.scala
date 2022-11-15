package dev.firework.amenities

import cats.effect.Async
import cats.effect.kernel.Resource
import com.comcast.ip4s.*
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server

trait MkHttpServer[F[_]]:
  
  def create(app: HttpApp[F]): Resource[F, Server]

end MkHttpServer


object MkHttpServer:
  
  def make[F[_] : Async]: MkHttpServer[F] = new MkHttpServer[F]:
    override def create(app: HttpApp[F]): Resource[F, Server] =
      EmberServerBuilder
        .default[F]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(app)
        .build
  
end MkHttpServer