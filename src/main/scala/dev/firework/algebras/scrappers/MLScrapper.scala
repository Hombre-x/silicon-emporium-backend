package dev.firework.algebras.scrappers

import cats.effect.{Async, Sync}

import org.jsoup.Jsoup

import dev.firework.domain.scrapper._



trait MLScrapper[F[_]] extends Scrapper[F]

object MLScrapper:
  
  def impl[F[_] : Sync]: Scrapper[F] = new Scrapper[F]:
      
    override def getMatchedElement(userQuery: UserQuery): F[ScrapperResult] =
      Sync[F].attempt(
        Sync[F].delay(
          Jsoup.connect(raw"https://listado.mercadolibre.com.co/$userQuery").get().title()
        )
      )
      
