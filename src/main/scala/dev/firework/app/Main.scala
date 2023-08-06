package dev.firework.app

import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.show.*

import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import dev.firework.amenities.*
import dev.firework.config.Config
import dev.firework.http.HttpApi
import dev.firework.instances.ConfigInstances.given

import natchez.Trace.Implicits.noop

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
  
  given logger: Logger[IO] = Slf4jLogger.getLoggerFromName("🌎App")

end Main
