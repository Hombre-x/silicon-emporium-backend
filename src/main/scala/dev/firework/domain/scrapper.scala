package dev.firework.domain

import eu.timepit.refined.types.string.NonEmptyString

import dev.firework.domain.search._


object scrapper:

  type Currency = Float
  type ScrapperResult = Either[Throwable, Item]
  type ErrorLog = List[String]
  type UserQuery = String 
  
end scrapper
