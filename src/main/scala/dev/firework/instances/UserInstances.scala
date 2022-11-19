package dev.firework.instances

import io.circe.*
import io.circe.generic.semiauto.*

import dev.firework.domain.user.{CreateUser, LoginUser}


object UserInstances:
  
  given CreateUserCodec: Codec[CreateUser] = deriveCodec
  given LoginUserCodec: Codec[LoginUser] = deriveCodec
 
end UserInstances
