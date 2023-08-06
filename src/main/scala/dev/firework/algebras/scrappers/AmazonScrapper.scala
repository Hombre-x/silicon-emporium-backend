package dev.firework.algebras.scrappers

import cats.effect.Sync
import cats.syntax.applicativeError.*

import dev.firework.domain.scrapper.*
import dev.firework.domain.search.Item

import org.jsoup.Jsoup

trait AmazonScrapper[F[_]] extends Scrapper[F]

object AmazonScrapper:

  def impl[F[_] : Sync]: Scrapper[F] = new Scrapper[F]:

    def formatPrice(price: String): Currency = ???

    // TODO: Amazon detects it is a bot, so refuses the connection
    override def getMatchedElement(userQuery: UserQuery): F[ScrapperResult] =
      Sync[F].delay: 
        val title = Jsoup.connect(raw"https://www.amazon.com/s?k=$userQuery").get().title()
        Item(title, 999F, "Amazon", "Amazon")
      .attempt
      
end AmazonScrapper
