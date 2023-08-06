package dev.firework.instances

import io.circe.Decoder.Result
import io.circe.*
import io.circe.generic.semiauto.*

import dev.firework.domain.search.*

object ItemInstances:
  
  // Standalone
  given ItemEncoder: Encoder[Item] = deriveEncoder
  given ItemDecoder: Decoder[Item] = deriveDecoder
  
  // Dedicated
  given BBItemEncoder: Encoder[BestBuyItem] = deriveEncoder
  given BBItemDecoder: Decoder[BestBuyItem] = deriveDecoder
  
end ItemInstances
