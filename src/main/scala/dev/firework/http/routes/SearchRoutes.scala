package dev.firework.http.routes

import cats.effect.Sync
import cats.syntax.all.*
import cats.{Monad, Parallel}
import dev.firework.algebras.scrappers.Scrapper
import dev.firework.core.Search
import dev.firework.domain.scrapper.ScrapperResult
import dev.firework.domain.search.Item
import dev.firework.instances.ItemInstances.given

import eu.timepit.refined.types.string.NonEmptyString
import eu.timepit.refined.auto._

import io.circe.syntax.*

import org.http4s.circe.*
import org.http4s.client.Client
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.server.Router
import org.http4s.{HttpApp, HttpRoutes}

import org.typelevel.log4cats.Logger


case class SearchRoutes[F[_]: Sync : Parallel : Logger](
    search: Search[F]
) extends Http4sDsl[F]:

  private val prefixPath = "/search"
  
  private object OptionalSearchQueryParamMatcher
    extends OptionalQueryParamDecoderMatcher[String]("keywords")

  // GET /search/query?keywords=Nvidia GeForce 3090 TI
  private def httpRoutes: HttpRoutes[F] =
    
    HttpRoutes.of[F] {
      
      case GET -> Root / "query" :? OptionalSearchQueryParamMatcher(maybeQuery) =>
        
        maybeQuery match
          case Some(query) =>
            
            for
              results <- search.perform(query)
              separated = search.separateErrors(results)
              _        <- Logger[F].info(s"${separated._1}")
              resp <- Ok(separated._2.asJson)
            yield resp

          case None => BadRequest("Can't perform an empty query")
    }

  end httpRoutes
  
  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)
  
end SearchRoutes
