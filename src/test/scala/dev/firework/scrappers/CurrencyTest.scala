package dev.firework.scrappers

import org.scalatest.funsuite._
import org.scalatest.matchers.should._

import dev.firework.domain.scrapper.Currency

class CurrencyTest extends AnyFunSuite with Matchers:
  def formatPriceML(price: String): Currency =
    price.filter(_.isDigit).toFloat
    
  def formatPriceEbay(price: String): Currency =
    price.filter(ch => ch.isDigit || ch == '.').toFloat

  test("The format currency function in ML should return a Float"){
    val price: String = "COP 10029394"
    val formattedPriceML = formatPriceML(price)
    
    formattedPriceML should be (10029394) 
  }
  
  test("The format currency function in eBay should return a Float"){
    val price: String = "COP 12934.01 $"
    val formattedPriceEbay = formatPriceEbay(price)

    formattedPriceEbay should be (12934.01F)
  }

end CurrencyTest