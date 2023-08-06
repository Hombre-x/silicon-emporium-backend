package http.client.algebras

import org.scalatest.funsuite.*
import org.scalatest.matchers.should.*

class BestBuyClientSuite extends AnyFunSuite with Matchers:

//  test("The format function should return the appropriate formatted Json given a response"){
//    def formatResponse(response: Json): Json =
//      val cursor: HCursor = response.hcursor
//      
//      val item = cursor.get[Item]("products")
//      
//      item match
//        case Right(value) => value.asJson
//        case Left(e)      => throw new Exception(s"Cans convert because $e")
//      
//    end formatResponse
//    
//    val response: Json = """{
//                           |    "canonicalUrl": "/v1/products(search=\"NVIDIA\"&search=\"3090\")?show=name,salePrice,url&pageSize=1&format=json&apiKey=qhqws47nyvgze2mq3qx4jadt",
//                           |    "currentPage": 1,
//                           |    "from": 1,
//                           |    "partial": false,
//                           |    "products": [
//                           |        {
//                           |            "name": "NVIDIA - GeForce RTX 3090 Ti - Titanium and black",
//                           |            "salePrice": 1099.99,
//                           |            "url": "https://api.bestbuy.com/click/-/6502626/pdp"
//                           |        }
//                           |    ],
//                           |    "queryTime": "0.012",
//                           |    "to": 1,
//                           |    "total": 28,
//                           |    "totalPages": 28,
//                           |    "totalTime": "0.016"
//                           |}""".stripMargin.asJson
//
//    (1 + 1) should be 1
//  }
  
end BestBuyClientSuite