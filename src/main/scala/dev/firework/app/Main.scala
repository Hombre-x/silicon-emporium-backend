package dev.firework.app

import cats.Monad
import cats.effect.{ExitCode, IO, IOApp}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import com.comcast.ip4s._

import org.http4s.HttpApp
import org.http4s.implicits._
import org.http4s.ember.server.EmberServerBuilder

import dev.firework.httpLayer.Controller.*

object Main extends IOApp:
  
  given logger: Logger[IO] = Slf4jLogger.getLogger
  
  override def run(args: List[String]): IO[ExitCode] =
    
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(allRoutesApp)
      .withLogger(Logger[IO])
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
    
  end run
  
end Main