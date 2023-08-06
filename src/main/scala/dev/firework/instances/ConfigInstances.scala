package dev.firework.instances

import cats.Show
import dev.firework.domain.config.AppConfig

object ConfigInstances:
  
  given ConfigShow: Show[AppConfig] = Show.show: config => 
    s"""
       |AppConfig(
       |  PostgresConfig( 
       |    host     = ${config.postgreSQL.host},
       |    port     = ${config.postgreSQL.port},
       |    user     = ${config.postgreSQL.user},
       |    password = ${config.postgreSQL.password},
       |    max      = ${config.postgreSQL.max},
       |  ),
       |   
       |  HttpServerConfig(
       |    host = ${config.httpServer.host},
       |    port = ${config.httpServer.port},
       |  )
       |)
       |""".stripMargin

end ConfigInstances
