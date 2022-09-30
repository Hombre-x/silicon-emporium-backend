package dev.firework.algebras.scrappers

import cats.effect.Sync
import cats.syntax.all.*

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import dev.firework.domain.scrapper.*
import dev.firework.domain.search.*
trait EbayScrapper[F[_]] extends Scrapper[F]

object EbayScrapper:

  def impl[F[_]: Sync]: Scrapper[F] = new Scrapper[F]:

    override def getMatchedElement(userQuery: UserQuery): F[ScrapperResult] =
      val url            = raw"https://www.ebay.com/sch/i.html?_nkw=$userQuery"

      val connectionDoc =
        Sync[F].delay(
          Jsoup
            .connect(url)
            .get()
            .getElementsByClass("s-item s-item__pl-on-bottom")
            .get(1)
            .getElementsByClass("s-item__info clearfix")
            .get(0)
        )

      val titleContainer =
        for
          container: Element <- connectionDoc
          title              <-
            Sync[F].delay(
              container
                .getElementsByClass("s-item__info clearfix")
                .get(0)
                .getElementsByTag("span")
                .get(0)
                .text()
            )
        yield title
      
      
      val priceContainer =
        for
          container: Element <- connectionDoc
          price              <-
            Sync[F].delay(
              container
                .getElementsByClass("s-item__detail s-item__detail--primary")
                .get(0)
                .getElementsByTag("span")
                .get(0)
                .text()
            )
        yield price

      for
        title  <- titleContainer
        price  <- priceContainer
        result <- Sync[F].pure(Item(title, price, "eBay")).attempt
      yield result

end EbayScrapper
