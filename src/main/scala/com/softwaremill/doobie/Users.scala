package com.softwaremill.doobie

import com.softwaremill.doobie.infra.CustomMeta
import com.softwaremill.doobie.model.{ User, UserAlreadyExists, UserError }
import com.softwaremill.doobie.sample.model.Id
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.postgres.sqlstate

class Users extends CustomMeta {

  def add(user: User): ConnectionIO[Unit] = {
    sql"insert into users (id, email, password_hash, referral_code, date_of_birth) values(${user.id}, ${user.email}, ${user.pwdHash}, ${user.referralCode}, ${user.dateOfBirth})".update.run
      .map(_ => ())
  }

  def safeAdd(user: User): ConnectionIO[Either[UserError, Unit]] = {
    (sql"insert into users (id, email, password_hash, referral_code, date_of_birth) values(${user.id}, ${user.email}, ${user.pwdHash}, ${user.referralCode}, ${user.dateOfBirth})".update.run
      .map(_ => ()): ConnectionIO[Unit])
      .attemptSomeSqlState({
        case sqlstate.class23.UNIQUE_VIOLATION => UserAlreadyExists
        case _                                 => throw new RuntimeException("can't help")
      })
  }

  def findById(id: Id): ConnectionIO[Option[User]] = {
    sql"select * from users where id = $id".query[User].option
  }

  def findAll(): ConnectionIO[List[User]] = {
    sql"select * from users".query[User].to[List]
  }

}
