package dev.firework.http.client.algebras

import cats.effect.Concurrent
import cats.syntax.all.*

import org.http4s.{Uri, _}
import org.http4s.circe.CirceEntityDecoder.circeEntityDecoder
import org.http4s.client.*
import org.http4s.implicits.*

import io.circe.{HCursor, Json, Decoder}

import dev.firework.domain.scrapper.UserQuery
import dev.firework.domain.search.{BestBuyItem, Item}
import dev.firework.instances.ItemInstances.given


trait BestBuyClient[F[_]]:
  
  def getItem(userQuery: UserQuery): F[Item]
  
end BestBuyClient


object BestBuyClient:
  
  def make[F[_] : Concurrent](client: Client[F]): BestBuyClient[F] = new BestBuyClient[F]:
    
    private val apiKey = "qhqws47nyvgze2mq3qx4jadt"
    
    private val baseUrl = raw"https://api.bestbuy.com/v1/products"
    
    private val urlParams = raw"?format=json&pageSize=1&show=name,salePrice,url&apiKey=$apiKey"
    
    private def formatQuery(query: UserQuery): UserQuery =
      query.split(' ')
        .map(str => s"&search=$str")
        .mkString
        .tail
      
    private def queryUri(query: UserQuery): Uri =
      Uri.fromString(raw"""$baseUrl(${formatQuery(query)})$urlParams""")
        .getOrElse(uri"/")
      
      
    private def formatResponse(response: Json): Decoder.Result[Item] =
      
      val cursor: HCursor = response.hcursor
      
      val bestBuyItem = cursor.downField("products").downArray.as[BestBuyItem]
      
      bestBuyItem.map(item => Item(item.name, item.salePrice * 4948, item.url, "Best Buy"))
      
    end formatResponse
    

    override def getItem(userQuery: UserQuery): F[Item] =
      for
        response <- client.expect[Json](queryUri(userQuery))
        item     <- Concurrent[F].fromEither(formatResponse(response))
      yield item
      
  
end BestBuyClient

