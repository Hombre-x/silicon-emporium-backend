package dev.firework.http.routes

import cats.Parallel
import cats.effect.Sync
import cats.syntax.all.*

import org.typelevel.log4cats.Logger

import io.circe.syntax.*

import org.http4s.dsl.impl.*
import org.http4s.dsl.*
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.http4s.circe.*


import dev.firework.core.Search
import dev.firework.instances.ItemInstances.given


case class SearchRoutes[F[_]: Sync : Parallel : Logger](
    search: Search[F]
) extends Http4sDsl[F]:

  private val prefixPath = "/search"
  
  private object OptionalSearchQueryParamMatcher
    extends OptionalQueryParamDecoderMatcher[String]("keywords")

  private def httpRoutes: HttpRoutes[F] =

    HttpRoutes.of[F] {

      // GET -> search/query?keywords=Nvidia GeForce 3090 TI
      case GET -> Root / "query" :? OptionalSearchQueryParamMatcher(maybeQuery) =>
        
        maybeQuery match
          case Some(query) if query.nonEmpty =>
            for
              results <- search.perform(query)
              separated = search.separateErrors(results)
              _        <- Logger[F].info(s"${separated._1}")
              resp <- Ok(separated._2.asJson)
            yield resp

          case Some(query) if query.isEmpty =>
            BadRequest("Can't perform an empty query")
            
          case _    => BadRequest("Can't perform an empty query")
    }

  end httpRoutes
  
  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)
  
end SearchRoutes
