package dev.firework.instances

import io.circe._
import io.circe.generic.semiauto._

import dev.firework.domain.search._

object ItemInstances:
  
  given ItemEncoder: Encoder[Item] = deriveEncoder
  
end ItemInstances
