package dev.firework.algebras

import cats.effect.Sync
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


trait Scrapper[F[_]]:

  def getQuery (url: String): F[Either[Throwable, String]]

end Scrapper


object Scrapper:

  def impl[F[_] : Sync]: Scrapper[F] =
    new Scrapper[F]:
      override def getQuery(url: String): F[Either[Throwable, String]] =        
        Sync[F].attempt(
          Sync[F].delay(
            Jsoup.connect(url).get().title()
          )
        )

end Scrapper


