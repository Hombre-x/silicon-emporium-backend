package dev.firework.algebras.scrappers

import cats.effect.Sync
import cats.syntax.all._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import dev.firework.domain.scrapper._


trait Scrapper[F[_]]:
  
  def getMatchedElement (userQuery: UserQuery): F[ScrapperResult]

end Scrapper


