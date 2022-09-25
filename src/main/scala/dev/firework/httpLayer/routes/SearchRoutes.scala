package dev.firework.httpLayer.routes

import cats.effect.Sync
import cats.syntax.all.*
import cats.{Monad, Parallel}
import dev.firework.algebras.scrappers.Scrapper
import dev.firework.core.Search
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.syntax.*
import org.http4s.circe.*
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.server.Router
import org.http4s.{HttpApp, HttpRoutes}
import org.typelevel.log4cats.Logger

class SearchRoutes[F[_]: Sync: Parallel: Logger] extends Http4sDsl[F]:

  private val prefixPath = "/search"

  private val httpRoutes: HttpRoutes[F] =
    
    HttpRoutes.of[F] {
      
      case GET -> Root / query =>
        
        val res = NonEmptyString from query
        
        res match
          case Right(value) =>
            val search      = Search[F](value)
            val querySearch =
              for
                results  <- search.perform
                separated = search.separateErrors(results)
                _        <- Logger[F].info(s"${separated._1}")
              yield separated._2
  
            Sync[F].flatMap(querySearch)(list => Ok(list.asJson))

          case Left(_) => BadRequest("Can't perform an empty query")
    }

  end httpRoutes
  
  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)
  
end SearchRoutes
