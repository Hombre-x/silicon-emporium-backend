package dev.firework.domain

import dev.firework.domain.scrapper.Currency


object search:
  
  // Standard Item Type
  case class Item(title: String, price: Currency, source: String)
  
  // Dedicated Site-Item Types, used for retrieve the Json response
  
  case class BestBuyItem(name: String, salePrice: Currency, url: String)
  
  
end search

