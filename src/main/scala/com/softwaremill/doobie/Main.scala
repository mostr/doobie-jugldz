package com.softwaremill.doobie

import java.time.LocalDate

import cats.effect.IO
import com.softwaremill.doobie.infra.{ Clock, Database, IdGen, UTCClock }
import com.softwaremill.doobie.model.{ User, UserError }
import doobie.implicits._

object Main extends App {

  val usersRepo         = new Users
  val verificationsRepo = new Verifications
  val idGen: IdGen      = new IdGen
  val clock: Clock      = UTCClock
  val xa                = Database.connect[IO]()

  val tommy = User(idGen.newId(), "tommy@example.com", "supersecret", None, LocalDate.of(1982, 8, 19))

  val addTommyIO: IO[Either[UserError, Unit]] = usersRepo.safeAdd(tommy).transact(xa)

  println(s"Add Tommy 1st try: ${addTommyIO.unsafeRunSync()}")
  println(s"Add Tommy 2nd try: ${addTommyIO.unsafeRunSync()}")

}
