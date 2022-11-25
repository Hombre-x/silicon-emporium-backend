package dev.firework.algebras.auth

import cats.MonadThrow
import cats.syntax.all.*
import org.typelevel.log4cats.Logger
import cats.effect.MonadCancelThrow

import com.dedipresta.crypto.hash.sha256.Sha256

import dev.firework.algebras.postgres.Users
import dev.firework.domain.user.*


trait Auth[F[_]]:

  def register(user: CreateUser): F[Username]
  def login(username: Username, password: Password): F[Unit]
  def changePass(changePassUser: ChangePassUser): F[Username]

end Auth

object Auth:

  def make[F[_] : MonadThrow : Logger](users: Users[F]): Auth[F] = new Auth[F]:
    
    override def register(user: CreateUser): F[Username] =
      
      users.find(user.username).flatMap {
            
        case Some(_) =>
            UserInUse(user.username).raiseError[F, Username]
        
        case None =>
          users.create(
            CreateUser(
              user.username,
              Sha256.hashString(user.password),
              user.names,
              user.surnames
            )
          )
      }
      
    end register
    
      
    override def login(username: Username, password: Password): F[Unit] =
      users.find(username).flatMap( (maybeUser: Option[UserFound]) =>
        maybeUser match
          case None => UserNotFound(username).raiseError[F, Unit]
          case Some(user) if user.password != Sha256.hashString(password) =>
            InvalidPassword(username).raiseError[F, Unit]
          case Some(user) => Logger[F].info(s"User: ${user.username} logged in.")
          
      )

    // TODO: Implement a better way to change user password, this is really insecure.
    override def changePass(changePassUser: ChangePassUser): F[Username] =
      users.find(changePassUser.username).flatMap{
        case None => UserNotFound(changePassUser.username).raiseError[F, Username]
        case Some(user) =>
          users.changePassword(
            user.username,
            Sha256.hashString(changePassUser.password)
          )
      }
      
      
    
      
  end make
  

