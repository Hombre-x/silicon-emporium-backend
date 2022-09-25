package dev.firework.domain

import eu.timepit.refined.types.string.NonEmptyString


object scrapper:

  type ScrapperResult = Either[Throwable, String]
  type ErrorLog = List[String]
  type UserQuery = NonEmptyString
  
end scrapper
