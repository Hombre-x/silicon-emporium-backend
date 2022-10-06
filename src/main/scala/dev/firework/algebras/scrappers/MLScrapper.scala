package dev.firework.algebras.scrappers

import cats.effect.Sync
import cats.syntax.all._

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import dev.firework.domain.scrapper._
import dev.firework.domain.search._

trait MLScrapper[F[_]] extends Scrapper[F]

object MLScrapper:
  
  def impl[F[_] : Sync]: Scrapper[F] = new Scrapper[F]:

    override def formatPrice(price: String): Currency = 
      price.filter(_.isDigit).toFloat
      
    override def getMatchedElement(userQuery: UserQuery): F[ScrapperResult] =
      
      val url = raw"https://listado.mercadolibre.com.co/$userQuery"
      val titleClassName = "ui-search-item__title shops__item-title"
      val priceClassName = "price-tag-text-sr-only"
      
      val connectionDoc = 
        Sync[F].delay(
          Jsoup.connect(url)
            .get()
            .getElementsByClass("ui-search-result__wrapper")
            .get(0)
        )
      
      for
        container: Element <- connectionDoc
        title     <-
          Sync[F].delay(container.getElementsByClass(titleClassName).get(0).text())
        price     <-
          Sync[F].delay(container.getElementsByClass(priceClassName).get(0).text())
        result    <- Sync[F].pure(Item(title, formatPrice(price), "Mercado Libre")).attempt
      yield result
      
    end getMatchedElement 
    
  end impl
  