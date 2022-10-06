package dev.firework.algebras.scrappers

import cats.effect.Sync
import org.jsoup.Jsoup

import dev.firework.domain.scrapper._
import dev.firework.domain.search._

trait BestbuyScrapper[F[_]] extends Scrapper[F]

object BestbuyScrapper:
  
  def impl[F[_] : Sync]: Scrapper[F] = new Scrapper[F]:

    override def formatPrice(price: String): Currency = ???
    
    override def getMatchedElement(userQuery: UserQuery): F[ScrapperResult] =
      Sync[F].attempt(
        Sync[F].delay{
          val title = 
            Jsoup.connect(raw"https://www.bestbuy.com/site/searchpage.jsp?st=$userQuery").get().title()
          Item(title, -1F , "Best Buy")
        }
      )