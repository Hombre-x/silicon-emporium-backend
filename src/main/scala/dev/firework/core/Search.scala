package dev.firework.core

import cats.Parallel
import cats.effect.Sync
import cats.syntax.all.*
import dev.firework.algebras.scrappers.*
import dev.firework.domain.scrapper.*
import eu.timepit.refined.types.string.NonEmptyString

case class Search[F[_] : Sync : Parallel](query: NonEmptyString):
  
  val scrappers: List[Scrapper[F]] =
    List(
      AmazonScrapper.impl[F],
      EbayScrapper.impl[F],
      MLScrapper.impl[F],
      BestbuyScrapper.impl[F]
    )
  
  def perform: F[List[ScrapperResult]] =
    scrappers.parTraverse(scrapp => scrapp.getMatchedElement(query))
    
  def separateErrors(eitherList: List[ScrapperResult]): (ErrorLog, List[String]) =
    eitherList.foldLeft((List.empty[String], List.empty[String])) ( (accum, next) =>
      next match
        case Right(result) => (accum._1, result :: accum._2)
        case Left(error)   => (s"$error" :: accum._1, accum._2)
    )
  

end Search