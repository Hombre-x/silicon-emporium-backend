package dev.firework.http.client.algebras

import cats.effect.Concurrent
import dev.firework.domain.scrapper.{ScrapperResult, UserQuery}

trait EbayClient[F[_]]:

  def getItem(userQuery: UserQuery): F[ScrapperResult]

end EbayClient


//object EbayClient:
//  def make[F[_] : Concurrent](client: Client[F]): EbayClient[F] = new EbayClient[F]:
//    override def getItem(userQuery: UserQuery): F[ScrapperResult] = ???
