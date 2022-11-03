package dev.firework.algebras.scrappers

import cats.effect.{Async, Sync}
import cats.{NonEmptyParallel, Parallel}
import cats.syntax.all.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import dev.firework.domain.scrapper.*
import dev.firework.domain.search.*
trait EbayScrapper[F[_]] extends Scrapper[F]

object EbayScrapper:

  def impl[F[_]: Sync : Parallel]: Scrapper[F] = new Scrapper[F]:

    def formatPrice(price: String): Either[Throwable, Currency] =
      Either.catchNonFatal(price.filter(ch => ch.isDigit || ch == '.').toFloat)
    
    
    override def getMatchedElement(userQuery: UserQuery): F[ScrapperResult] =
      
      val url = raw"https://www.ebay.com/sch/i.html?_nkw=$userQuery"

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
        

      val titleContainer: F[String] =
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
      
      
      val priceContainer: F[String] =
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
        

      val result: F[Item] =  for
        t  <- (titleContainer, priceContainer).parTupled
        (title, price) = t
        formattedPrice <- formatPrice(price).liftTo[F]
      yield Item(title, formattedPrice, "ebay")
      
      result.attempt

end EbayScrapper
