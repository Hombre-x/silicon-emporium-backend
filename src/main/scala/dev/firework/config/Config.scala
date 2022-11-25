package dev.firework.config

import cats.syntax.parallel.*
import cats.effect.Async

import ciris.{ConfigValue, env, file}

import com.comcast.ip4s.*

import dev.firework.domain.config.*

object Config:

  private def default[F[_]]: ConfigValue[F, AppConfig] =
    (
      env("SE_DATABASE_URL").default("localhost").as[String],
      env("SE_DATABASE_USERNAME").default("postgres").as[String],
      env("SE_DATABASE_PASSWORD").default("postgres").as[String].secret,
      env("SE_DATABASE_PORT").default("5432").as[Int],
      env("SE_API_HTTP_PORT").default("80").as[Int]
    ).parMapN(
      
      (dbUrl, dbUser, dbPassword, dbPort, httpPort) =>
        
        AppConfig(
          
          postgreSQL =
            PostgreSQLConfig(
              host = dbUrl,
              port = dbPort,
              user = dbUser,
              password = dbPassword,
              max = 10
            ),

          httpServer =
            HttpServerConfig(
              host = ipv4"0.0.0.0",
              port = Port.fromInt(httpPort).getOrElse(port"80")
            )
            
        )
    )
    
    
  def load[F[_] : Async]: F[AppConfig] = default[F].load[F]
  
end Config
