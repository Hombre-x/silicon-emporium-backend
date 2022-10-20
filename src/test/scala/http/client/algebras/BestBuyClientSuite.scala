package http.client.algebras

import io.circe.{Json, HCursor}
import io.circe.syntax.*
import dev.firework.domain.search.Item
import dev.firework.instances.ItemInstances.given
import org.scalatest.funsuite.*
import org.scalatest.matchers.should.*

class BestBuyClientSuite extends AnyFunSuite with Matchers:

  test("The format function should return the appropriate formatted Json given a response"){
    def formatResponse(response: Json): Json =
      val cursor: HCursor = response.hcursor
      
      val item = cursor.get[Item]("products")
      
      item.asJson
      
    formatResponse()
  }
  
end BestBuyClientSuite