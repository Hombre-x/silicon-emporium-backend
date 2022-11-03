package dev.firework.algebras.scrappers

import cats.Parallel
import cats.effect.Sync
import cats.syntax.all.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import dev.firework.domain.scrapper.*
import dev.firework.domain.search.*

trait MLScrapper[F[_]] extends Scrapper[F]

object MLScrapper:
  
  def impl[F[_] : Sync : Parallel]: Scrapper[F] = new Scrapper[F]:

    def formatPrice(price: String): Currency =
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
        
      val titleContainer: F[String] = for
        container <- connectionDoc
        title <-
          Sync[F].delay(
            container.getElementsByClass(titleClassName).get(0).text()
          )
      yield title
        
      val priceContainer: F[String] = for
        container <- connectionDoc
        price <-
          Sync[F].delay(
            container.getElementsByClass(priceClassName).get(0).text()
          )
      yield price
      
      
      val result: F[Item] =
        for
          tup <- (titleContainer, priceContainer).parTupled
          (title, price) = tup
        yield Item(title, formatPrice(price), "Mercado Libre")
        
      result.attempt
      
    end getMatchedElement 
    
  end impl
  