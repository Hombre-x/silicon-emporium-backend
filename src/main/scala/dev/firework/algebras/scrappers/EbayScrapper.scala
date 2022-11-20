package dev.firework.algebras.scrappers

import cats.effect.{Async, Sync}
import cats.syntax.all.*
import cats.{NonEmptyParallel, Parallel}
import dev.firework.domain.scrapper.*
import dev.firework.domain.search.*
import dev.firework.utils.debug
import fs2.{Chunk, Stream}
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements

trait EbayScrapper[F[_]] extends Scrapper[F]

object EbayScrapper:

  def impl[F[_] : Sync : Parallel]: Scrapper[F] = new Scrapper[F] :

    def formatPrice(price: String): Either[Throwable, Currency] =
      Either.catchNonFatal(price.filter(ch => ch.isDigit || ch == '.').toFloat)
    
    
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
          doc: Document <- connectionDoc
          item = getFirstItem(doc)
          tup <-
            (
              getTitle(item).pure[F],
              getPrice(item).pure[F],
              getSource(item).pure[F]
            ).parTupled
          (title, price, source) = tup
          formattedPrice <- formatPrice(price).liftTo[F]
        yield Item(title, formattedPrice, source)

      result.attempt

end EbayScrapper
