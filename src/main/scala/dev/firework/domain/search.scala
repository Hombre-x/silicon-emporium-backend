package dev.firework.domain

import dev.firework.domain.scrapper.Currency


object search:
  
  case class Item(title: String, price: Currency, source: String) extends AnyRef
  
end search

