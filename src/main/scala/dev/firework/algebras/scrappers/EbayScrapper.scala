package dev.firework.algebras.scrappers

import cats.{ApplicativeThrow, Parallel}
import cats.effect.Sync
import cats.syntax.all.*

import dev.firework.domain.scrapper.*
import dev.firework.domain.search.*

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}


trait EbayScrapper[F[_]] extends Scrapper[F]

object EbayScrapper:

  def impl[F[_] : ApplicativeThrow : Sync : Parallel ]: Scrapper[F] = new Scrapper[F]:

    def formatPrice(price: String): F[Currency] =
      ApplicativeThrow[F].catchNonFatal: 
        price.filter(ch => ch.isDigit || ch == '.').toFloat
    
    
    override def getMatchedElement(userQuery: UserQuery): F[ScrapperResult] =
      
      val url = raw"https://www.ebay.com/sch/i.html?_nkw=$userQuery"

      val connectionDoc =
        Sync[F].delay(
          Jsoup.connect(url).get()
        )
        

      def getFirstItem(doc: Document): Element =
        doc
          .getElementsByClass("srp-results srp-list clearfix").first
          .getElementsByClass("s-item__wrapper clearfix").first
          
        
      def getTitle(item: Element): String =
        item
          .getElementsByClass("s-item__title").first
          .getElementsByTag("span").first.text
        
        
      def getPrice(item: Element): String =
        item
          .getElementsByClass("s-item__price").first
          .getElementsByTag("span").first.text
        
        
      def getSource(item: Element): String =
        item
          .getElementsByClass("s-item__link")
          .select("a").first
          .attr("href")
      

      val result: F[Item] =
        for
          doc <- connectionDoc
          item = getFirstItem(doc)
          tup <-
            (
              getTitle(item).pure[F],
              getPrice(item).pure[F],
              getSource(item).pure[F]
            ).parTupled
          (title, price, source) = tup
          formattedPrice <- formatPrice(price)
        yield Item(title, formattedPrice * 4948, source, "eBay")
        
      result.attempt

end EbayScrapper
