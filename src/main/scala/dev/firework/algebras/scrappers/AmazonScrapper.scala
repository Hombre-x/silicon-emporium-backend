package dev.firework.algebras.scrappers

import dev.firework.algebras.scrappers.Scrapper

import dev.firework.domain.scrapper._

import org.jsoup.Jsoup

import cats.effect.Sync

trait AmazonScrapper[F[_]] extends Scrapper[F]

object AmazonScrapper:

  def impl[F[_] : Sync]: Scrapper[F] = new Scrapper[F]:

    // TODO: Amazon detects it is a bot, so refuses the connection
    override def getMatchedElement(userQuery: UserQuery): F[ScrapperResult] =
      Sync[F].attempt(
        Sync[F].delay(
          Jsoup.connect(raw"https://www.amazon.com/s?k=$userQuery").get().title()
        )
      )
end AmazonScrapper
