package dev.firework.algebras.scrappers

import dev.firework.algebras.scrappers.Scrapper
import dev.firework.domain.scrapper.*
import org.jsoup.Jsoup
import cats.effect.Sync
import dev.firework.domain.search.Item

trait AmazonScrapper[F[_]] extends Scrapper[F]

object AmazonScrapper:

  def impl[F[_] : Sync]: Scrapper[F] = new Scrapper[F]:

    def formatPrice(price: String): Currency = ???

    // TODO: Amazon detects it is a bot, so refuses the connection
    override def getMatchedElement(userQuery: UserQuery): F[ScrapperResult] =
      Sync[F].attempt(
        Sync[F].delay {
          val title = Jsoup.connect(raw"https://www.amazon.com/s?k=$userQuery").get().title()
          Item(title, 999F, "Amazon", "Amazon")
        }
      )
      
end AmazonScrapper
