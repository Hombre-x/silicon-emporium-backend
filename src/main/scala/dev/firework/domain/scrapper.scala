package dev.firework.domain

import dev.firework.domain.search.*


object scrapper:

  type Currency = Float
  type ScrapperResult = Either[Throwable, Item]
  type ErrorLog = List[String]
  type UserQuery = String 
  
end scrapper
