package com.softwaremill.doobie

import java.time.LocalDate

import cats.effect.IO
import com.softwaremill.doobie.infra.{ Database, IdGen }
import com.softwaremill.doobie.model.User
import doobie.free.connection.ConnectionIO
import doobie.implicits._

object Main extends App {

  val usersRepo    = new Users
  val idGen: IdGen = new IdGen
  val xa           = Database.connect[IO]()

  val tommy = User(idGen.newId(), "tommy@example.com", "supersecret", None, LocalDate.of(1982, 8, 19))

  val cio: ConnectionIO[Unit] = usersRepo.add(tommy)
  val eff: IO[Unit]           = cio.transact(xa)

  // finally run it
  eff.unsafeRunSync()

  val allUsers: Seq[User] = usersRepo.findAll().transact(xa).unsafeRunSync()

}
