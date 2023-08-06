package dev.firework.core

import cats.Parallel
import cats.effect.Sync
import cats.syntax.all.*

import org.http4s.client.Client

import dev.firework.algebras.scrappers.*
import dev.firework.amenities.AppClients
import dev.firework.domain.scrapper.*
import dev.firework.domain.search.*
import dev.firework.utils.debug

case class Search[F[_] : Sync : Parallel](clients: AppClients[F]):
  
  // TODO: Maybe using Stream.attempt we can simplify the code.
  
  private val scrappers: List[Scrapper[F]] =
    List(
      // AmazonScrapper.impl[F],
      EbayScrapper.impl[F],
      MLScrapper.impl[F],
      BestbuyScrapper.impl[F](clients.bestBuyClient)
    )
  
  def perform(query: UserQuery): F[List[ScrapperResult]] =
    scrappers.parTraverse(scrapp => scrapp.getMatchedElement(query))
    
    
  def separateErrors(eitherList: List[ScrapperResult]): (ErrorLog, List[Item]) =
    eitherList.foldLeft((List.empty[String], List.empty[Item])) ( (accum, next) =>
      next match
        case Right(result) => (accum._1, result :: accum._2)
        case Left(error)   => (s"$error" :: accum._1, accum._2)
    )
  

end Search