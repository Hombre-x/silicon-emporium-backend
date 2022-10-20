package dev.firework.http.client.algebras

import cats.effect.Concurrent
import cats.syntax.all.*

import org.http4s.client.*
import org.http4s.*
import org.http4s.implicits.uri
import org.http4s.Uri
import org.http4s.circe.*
import org.http4s.client.dsl.Http4sClientDsl

import io.circe.Json
import io.circe.syntax.EncoderOps
import io.circe.generic.auto.*

import eu.timepit.refined.auto._
import eu.timepit.refined.string._
import eu.timepit.refined.types.string.NonEmptyString


import dev.firework.domain.scrapper.{ScrapperResult, UserQuery}
import dev.firework.domain.search.Item


trait BestBuyClient[F[_]]:
  
  def getMatchedElement(userQuery: UserQuery): F[ScrapperResult]
  
end BestBuyClient


object BestBuyClient:
  
  def impl[F[_] : Concurrent](client: Client[F]): BestBuyClient[F] = new BestBuyClient[F]:
    
    private val apiKey = "qhqws47nyvgze2mq3qx4jadt"
    
    private def formatQuery(query: UserQuery): UserQuery =
      query.toString.split(' ')
        .map(str => s"&search=$str")
        .tail
        .mkString
        .asInstanceOf[NonEmptyString] // TODO: Change this implementation to something better
      
    private def formatResponse(response: Json): Json = ???
      
    private val queryUri: UserQuery => Uri = query => ???
//      uri"""https://api.bestbuy.com/v1/products(${formatQuery(query)})?format=json&pageSize=1&show=name,salePrice&apiKey=qhqws47nyvgze2mq3qx4jadt"""
      
    
    override def getMatchedElement(userQuery: UserQuery): F[ScrapperResult] =
      Item("Test", 0F, "eBay").pure[F].attempt
      
//      client.expect[String](queryUri(userQuery)).attempt
  
end BestBuyClient

