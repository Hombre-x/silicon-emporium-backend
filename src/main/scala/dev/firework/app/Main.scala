package dev.firework.app

import cats.syntax.show.*
import cats.effect.{ExitCode, IO, IOApp}

import com.comcast.ip4s.*

import org.http4s.HttpApp
import org.http4s.implicits.*
import org.http4s.ember.server.EmberServerBuilder

import natchez.Trace.Implicits.noop

import dev.firework.http.HttpApi
import dev.firework.amenities.*
import dev.firework.domain.config.AppConfig
import dev.firework.config.Config
import dev.firework.instances.ConfigInstances.given

object Main extends IOApp:

  override def run(args: List[String]): IO[ExitCode] = Config
    .load[IO]
    .flatMap: cfg =>
      logger.info(s"Loading configurations: ${cfg.show}") >>
        AppResources.make[IO](cfg).use: resources =>

          val clients  = AppClients.make(resources.client)
          val program  = AppPrograms.make(clients)
          val security = AppSecurity.make(resources.postgres)
          val api      = HttpApi.make(program, security)
          val server   = MkHttpServer.make[IO](cfg.httpServer).create(api.httpApp)

          server.useForever
            .onCancel(logger.info("Closing server..."))
            .as(ExitCode.Success)

  end run
  given logger: Logger[IO] = Slf4jLogger.getLoggerFromName("Main")

end Main
