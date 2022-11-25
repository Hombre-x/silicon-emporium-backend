package dev.firework.instances

import io.circe.*
import io.circe.generic.semiauto.*
import dev.firework.domain.user.{ChangePassUser, CreateUser, LoginUser}


object UserInstances:
  
  given CreateUserCodec: Codec[CreateUser] = deriveCodec
  given LoginUserCodec: Codec[LoginUser] = deriveCodec
  given ChangeUserCodec: Codec[ChangePassUser] = deriveCodec
 
end UserInstances
