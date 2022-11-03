package dev.firework.http.client.algebras

import cats.effect.Concurrent
import cats.syntax.all.*

import org.http4s.client.*
import org.http4s.*
import org.http4s.implicits.*
import org.http4s.Uri
import org.http4s.circe.CirceEntityDecoder.circeEntityDecoder

import io.circe.Json
import io.circe.HCursor
import io.circe.syntax.EncoderOps
import io.circe.generic.auto.*

import eu.timepit.refined.auto._
import eu.timepit.refined.string._
import eu.timepit.refined.types.string.NonEmptyString


import dev.firework.domain.scrapper.{ScrapperResult, UserQuery}
import dev.firework.domain.search.{Item, BestBuyItem}
import dev.firework.instances.ItemInstances.given


trait BestBuyClient[F[_]]:
  
  def getItem(userQuery: UserQuery): F[ScrapperResult]
  
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
      
      
    private def formatResponse(response: Json): Either[Throwable, Item] =
      
      val cursor: HCursor = response.hcursor
      
      val bestBuyItem = cursor.downField("products").downArray.as[BestBuyItem]
      
      bestBuyItem.map(item => Item(item.name, item.salePrice, item.url))
      
    end formatResponse
    

    override def getItem(userQuery: UserQuery): F[ScrapperResult] =
      for
        response: Either[Throwable, Json] <-
          client.expect[Json](queryUri(userQuery)).attempt
        maybeItem = response.flatMap(jsonItem => formatResponse(jsonItem))
      yield maybeItem
      
  
end BestBuyClient

