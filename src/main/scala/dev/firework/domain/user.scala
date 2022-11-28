package dev.firework.domain

import scala.util.control.NoStackTrace

object user:
  
  type UserID = Long
  type Username = String
  type Password = String // Limited to 256

  case class CreateUser(
      username: String,
      password: String,
      names: String,
      surnames: String
  )
  
  case class UserName(username: Username)
  
  case class LoginUser(username: Username, password: Password)
  
  case class ChangePassUser(username: Username, password: Password)
  
  case class UserInUse(username: Username) extends  NoStackTrace
  case class UserNotFound(username: Username) extends NoStackTrace
  case class InvalidPassword(username: Username) extends NoStackTrace
  case class UserFound(id: UserID, username: Username, password: Password) //TODO: Create login and auth domain
