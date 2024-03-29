package dev.firework.algebras.postgres

import cats.effect.kernel.MonadCancelThrow
import cats.syntax.all.*

import org.typelevel.log4cats.Logger

import skunk.*
import skunk.syntax.all.*

import dev.firework.domain.skunkTypes.Pool
import dev.firework.domain.user.*
import dev.firework.sql.SkunkCodecs.*


trait Users[F[_]]:

  def find(username: String): F[Option[UserFound]]
  def create(user: CreateUser): F[Username]
  def changePassword(username: Username, newPassword: Password): F[Username]

end Users


object Users:
  
  def make[F[_] : MonadCancelThrow : Logger](postgres: Pool[F]): Users[F] = new Users[F]:
    
    import UsersSQL.{createUser, selectUser, changeUser}

    override def find(username: Username): F[Option[UserFound]] =
      postgres.use: se =>
        se.prepare(selectUser).flatMap( ps =>
            ps.option(username)
          )
    
    override def create(user: CreateUser): F[Username] =
      postgres.use: se =>
        se.prepare(createUser).flatMap( cmd =>
          cmd
            .execute(user)
            .as(user.username)
            .recoverWith:
              case e => e.raiseError[F, Username]
        )

    override def changePassword(username: Username, password: Password): F[Username] =
      postgres.use: se =>
        se.prepare(changeUser).flatMap( cmd =>
          cmd
            .execute(password ~ username)
            .as(username)
            .recoverWith:
              case e => e.raiseError[F, Username]
        )

  private object UsersSQL:

    val encoder: Encoder[CreateUser] =
      (username ~ password ~ name ~ surname)
        .values
        .gcontramap[CreateUser]
    
    val decoder: Decoder[UserFound] =
      (userId ~ username ~ password)
        .map {case i ~ u ~ p => UserFound(i, u, p)}
    
  
    val selectUser: Query[Username, UserFound] =
      sql"""
           select user_id, username, password from "user"
           where username = $username;
         """.query(decoder)
      
    val createUser: Command[CreateUser] =
      sql"""
           insert into "user" ("username", "password", "names", "surnames")
           values ($username, $password, $name, $surname);
         """.command.gcontramap[CreateUser]
      
      
    val changeUser: Command[Password ~ Username] =
      sql"""
           update "user"
           set "password" = $password
           where username = $username;
         """.command
    
  end UsersSQL
  
end Users

