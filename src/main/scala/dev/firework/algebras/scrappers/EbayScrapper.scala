package dev.firework.algebras.scrappers

import cats.effect.Sync
import org.jsoup.Jsoup

import dev.firework.domain.scrapper._

trait EbayScrapper[F[_]] extends Scrapper[F]

object EbayScrapper:
  
  def impl[F[_] : Sync]: Scrapper[F] = new Scrapper[F]:
      
    override def getMatchedElement(userQuery: UserQuery): F[ScrapperResult] =
      Sync[F].attempt(
        Sync[F].delay(
          Jsoup.connect(raw"https://www.ebay.com/sch/i.html?_nkw=$userQuery").get().title()
        )
      )
      
  
end EbayScrapper


