package dev.firework.sql

import skunk.*
import skunk.codec.numeric.int8
import skunk.codec.text.varchar

import dev.firework.domain.user.*
object SkunkCodecs:
  
  val userId: Codec[UserID]     = int8
  val username: Codec[Username] = varchar
  val password: Codec[Password] = varchar(256)
  val name: Codec[String]       = varchar
  val surname: Codec[String]    = varchar
 
end SkunkCodecs
