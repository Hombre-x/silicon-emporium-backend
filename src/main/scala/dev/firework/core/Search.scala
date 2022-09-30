package dev.firework.core

import cats.Parallel
import cats.effect.Sync
import cats.syntax.all.*
import dev.firework.algebras.scrappers.*
import dev.firework.domain.scrapper.*
import dev.firework.domain.search._
import dev.firework.utils.DebugUtils._
import eu.timepit.refined.types.string.NonEmptyString

case class Search[F[_] : Sync : Parallel](query: NonEmptyString):
  
  private val scrappers: List[Scrapper[F]] =
    List(
      AmazonScrapper.impl[F],
      EbayScrapper.impl[F],
      MLScrapper.impl[F]
//      BestbuyScrapper.impl[F]
    )
  
  def perform: F[List[ScrapperResult]] =
    scrappers.parTraverse(scrapp => scrapp.getMatchedElement(query))
    
  def performTest: F[List[Unit]] =
    scrappers.parTraverse(_ => Sync[F].delay("Testing...").debug.void)
    
  def separateErrors(eitherList: List[ScrapperResult]): (ErrorLog, List[Item]) =
    eitherList.foldLeft((List.empty[String], List.empty[Item])) ( (accum, next) =>
      next match
        case Right(result) => (accum._1, result :: accum._2)
        case Left(error)   => (s"$error" :: accum._1, accum._2)
    )
  

end Search