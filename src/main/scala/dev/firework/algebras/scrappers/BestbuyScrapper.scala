package dev.firework.algebras.scrappers

import dev.firework.domain.scrapper.*
import dev.firework.http.client.algebras.BestBuyClient

trait BestbuyScrapper[F[_]] extends Scrapper[F]

object BestbuyScrapper:
  
  def impl[F[_]](bestBuyClient: BestBuyClient[F]): Scrapper[F] = new Scrapper[F]:
    override def getMatchedElement(userQuery: UserQuery): F[ScrapperResult] =
      bestBuyClient.getItem(userQuery)