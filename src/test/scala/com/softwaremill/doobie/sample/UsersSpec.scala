package com.softwaremill.doobie.sample

import java.time.LocalDate

import cats.effect.IO
import com.softwaremill.doobie.infra.{ CustomMeta, Database, IdGen }
import com.softwaremill.doobie.model.User
import doobie.implicits._
import doobie.scalatest._
import doobie.util.transactor.Transactor
import doobie.util.update.Update0
import org.scalatest.FlatSpec

class UsersSpec extends FlatSpec with IOChecker with CustomMeta {

  implicit val idGen: IdGen = new IdGen

  override def transactor: Transactor[IO] = Database.connect[IO]()

  val Tommy = User(idGen.newId(), "tommy@example.com", "supersecret", None, LocalDate.of(1982, 8, 19))

  def addUser(user: User): Update0 = {
    sql"insert into users (id, email, password_hash, referral_code, date_of_birth) values(${user.id}, ${user.email}, ${user.pwdHash}, ${user.referralCode}, ${user.dateOfBirth})".update
  }

  it should "typecheck insert" in {
    check(addUser(Tommy))
  }

}
