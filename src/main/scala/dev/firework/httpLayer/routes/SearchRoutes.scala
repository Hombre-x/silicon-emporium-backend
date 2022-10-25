package dev.firework.httpLayer.routes

import cats.effect.Sync
import cats.syntax.all.*
import cats.{Monad, Parallel}
import org.typelevel.log4cats.Logger

import dev.firework.algebras.scrappers.Scrapper
import dev.firework.core.Search
import dev.firework.domain.scrapper.ScrapperResult
import dev.firework.domain.search.Item
import dev.firework.instances.ItemInstances.given

import eu.timepit.refined.types.string.NonEmptyString

import io.circe.syntax.*

import org.http4s.circe.*
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.server.Router
import org.http4s.{HttpApp, HttpRoutes}


class SearchRoutes[F[_]: Sync: Parallel: Logger] extends Http4sDsl[F]:

  private val prefixPath = "/search"

  private val httpRoutes: HttpRoutes[F] =
    
    HttpRoutes.of[F] {
      
      case GET -> Root / query =>
        
        val res = NonEmptyString from query
        
        res match
          case Right(value) =>
            
            val search = Search[F](value)
            
            for
              results <- search.perform
              separated = search.separateErrors(results) 
              _        <- Logger[F].info(s"${separated._1}")
              resp <- Ok(separated._2.asJson)
            yield resp            
            
          case Left(_) => BadRequest("Can't perform an empty query")
    }

  end httpRoutes
  
  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)
  
end SearchRoutes
