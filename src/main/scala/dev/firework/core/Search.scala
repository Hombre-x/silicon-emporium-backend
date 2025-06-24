package dev.firework.core

import cats.Parallel
import cats.syntax.all.*
import cats.effect.Async

import fs2.Stream

import dev.firework.algebras.scrappers.*
import dev.firework.amenities.AppClients
import dev.firework.domain.scrapper.*
import dev.firework.domain.search.*
import org.typelevel.log4cats.Logger

case class Search[F[_] : Async : Parallel : Logger](clients: AppClients[F]):
  
  // TODO: Maybe using Stream.attempt we can simplify the code.
  
  private val scrappers: List[Scrapper[F]] =
    List(
      // AmazonScrapper.impl[F],
      EbayScrapper.impl[F],
      MLScrapper.impl[F],
      BestbuyScrapper.impl[F](clients.bestBuyClient)
    )

  def perform(query: UserQuery): Stream[F, Item] =
      Stream.emits(scrappers)
        .parEvalMapUnorderedUnbounded(_.getMatchedElement(query))
        .handleErrorWith: e =>
          Stream.eval(Logger[F].error(s"Error while scrapping: $e")).drain
            
end Search