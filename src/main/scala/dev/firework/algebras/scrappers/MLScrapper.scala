package dev.firework.algebras.scrappers

import cats.Parallel
import cats.effect.Sync
import cats.syntax.all.*

import dev.firework.domain.scrapper.*
import dev.firework.domain.search.*

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

trait MLScrapper[F[_]] extends Scrapper[F]

object MLScrapper:
  
  def impl[F[_] : Sync : Parallel]: Scrapper[F] = new Scrapper[F]:

    def formatPrice(price: String): Currency =
      price.filter(_.isDigit).toFloat
      
    override def getMatchedElement(userQuery: UserQuery): F[Item] =
      
      val url = s"""https://listado.mercadolibre.com.co/$userQuery"""
      val titleClassName = "ui-search-item__title shops__item-title"
      val priceClassName = "price-tag-text-sr-only"
      
      val connectionDoc = Sync[F].delay:
        Jsoup.connect(url).get()
        
        
      def getFirstItem(doc: Document): Element =
        doc.getElementsByClass("ui-search-result__wrapper").first


      def getTitle(item: Element): String =
        item.getElementsByClass(titleClassName).first.text
        

      def getPrice(item: Element): String =
        item.getElementsByClass(priceClassName).first.text
        
        
      def getSource(item: Element): String =
        item.getElementsByClass("ui-search-link").first.attr("href")


      // Getting the final result
      val result: F[Item] =
        for
          doc <- connectionDoc
          item = getFirstItem(doc)
          finalItem <-
            (
              getTitle(item).pure[F],
              formatPrice(getPrice(item)).pure[F],
              getSource(item).pure[F],
              "Mercado Libre".pure[F]
            ).parMapN(Item.apply)
        yield finalItem
        
      result
      
    end getMatchedElement 
    
  end impl
  