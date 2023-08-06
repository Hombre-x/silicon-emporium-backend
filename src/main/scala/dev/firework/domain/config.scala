package dev.firework.domain

import ciris.Secret

import com.comcast.ip4s.{Host, Port}
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
      host: Host,
      port: Port
  )
 
end config
