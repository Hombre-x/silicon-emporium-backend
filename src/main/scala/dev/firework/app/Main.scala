package dev.firework.app

import cats.Monad
import cats.syntax.all.*
import cats.effect.{ExitCode, IO, IOApp, Resource}

import org.typelevel.log4cats.{Logger, LoggerFactory, LoggerName, SelfAwareStructuredLogger}
import org.typelevel.log4cats.slf4j.{Slf4jFactory, Slf4jLogger, loggerFactoryforSync}

import com.comcast.ip4s.*

import org.http4s.HttpApp
import org.http4s.implicits.*
import org.http4s.ember.server.EmberServerBuilder

import natchez.Trace.Implicits.noop

import dev.firework.http.HttpApi
import dev.firework.amenities.*
import dev.firework.domain.config.AppConfig
import dev.firework.config.Config

object Main extends IOApp:
  
  given logger: Logger[IO] = Slf4jLogger.getLoggerFromName("Main")
  
  override def run(args: List[String]): IO[ExitCode] =
    
    logger.info("Loading configurations...") >> Config.load[IO].flatMap( cfg =>
      
      logger.info("Starting server...") >> AppResources.make[IO](cfg).use { resources =>

        val clients = AppClients.make(resources.client)
        val program = AppPrograms.make(clients)
        val security = AppSecurity.make(resources.postgres)
        val api = HttpApi.make(program, security)
        val server = MkHttpServer.make[IO].create(api.httpApp)

        server
          .useForever
          .onCancel(logger.info("Closing server..."))
          .as(ExitCode.Success)

      }
    )
    
  end run
  
end Main