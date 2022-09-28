package dev.firework.algebras.scrappers

import cats.effect.{Async, Sync}
import cats.syntax.all._

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import dev.firework.domain.scrapper._


trait MLScrapper[F[_]] extends Scrapper[F]

object MLScrapper:
  
  def impl[F[_] : Sync]: Scrapper[F] = new Scrapper[F]:
      
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
            .getElementsByClass("ui-search-result__content-wrapper shops-custom-secondary-font")
            .get(0)
        )
        
      val finalTest =
        for
          container: Element <- connectionDoc
          title     <-
            Sync[F].delay(container.getElementsByClass(titleClassName).get(0).text())
          price     <-
            Sync[F].delay(container.getElementsByClass(priceClassName).get(0).text())
          result    <- Sync[F].pure(s"($title, $price)").attempt
        yield result
        
      finalTest
      
    end getMatchedElement
    
  end impl
  