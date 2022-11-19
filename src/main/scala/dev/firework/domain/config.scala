package dev.firework.domain

import ciris.Secret
object config:
  
  case class AppConfig(
      postgreSQL: PostgreSQLConfig,
      httpServer: HttpServerConfig
  )
  
  case class PostgreSQLConfig(
      host: String,
      port: Int,
      user: String,
      password: Secret[String],
      max: Int
  )
  
  case class HttpServerConfig(
      host: String,
      port: String
  )
 
end config
