package dev.firework.app

import cats.Monad
import cats.syntax.all._
import cats.effect.{ExitCode, IO, IOApp}
import org.typelevel.log4cats.{Logger, LoggerFactory, LoggerName, SelfAwareStructuredLogger}
import org.typelevel.log4cats.slf4j.{Slf4jFactory, Slf4jLogger, loggerFactoryforSync}

import com.comcast.ip4s.*
import org.http4s.HttpApp
import org.http4s.implicits.*
import org.http4s.ember.server.EmberServerBuilder
import dev.firework.http.Controller.*

object Main extends IOApp:
  
  given logger: Logger[IO] = Slf4jLogger.getLoggerFromName("logger")
  override def run(args: List[String]): IO[ExitCode] =
    
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(allRoutesApp)
      .withLogger(logger)
      .build
      .useForever
      .as(ExitCode.Success)
    
  end run
  
end Main