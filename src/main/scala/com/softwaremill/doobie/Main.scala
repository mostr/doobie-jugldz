package com.softwaremill.doobie

import java.time.LocalDate

import cats.effect.IO
import com.softwaremill.doobie.infra.{ Clock, Database, IdGen, UTCClock }
import com.softwaremill.doobie.model.{ IdVerificationStatus, PoRVerificationStatus, User }
import doobie.free.connection.ConnectionIO
import doobie.implicits._

object Main extends App {

  val usersRepo         = new Users
  val verificationsRepo = new Verifications
  val idGen: IdGen      = new IdGen
  val clock: Clock      = UTCClock
  val xa                = Database.connect[IO]()

  val tommy = User(idGen.newId(), "tommy@example.com", "supersecret", None, LocalDate.of(1982, 8, 19))

  val cio: ConnectionIO[Unit] = usersRepo.add(tommy)
  val eff: IO[Unit]           = cio.transact(xa)

  // finally run it
  eff.unsafeRunSync()

  val allUsers: Seq[User] = usersRepo.findAll().transact(xa).unsafeRunSync()

  val idCreated = verificationsRepo.add(tommy.id, IdVerificationStatus.IdSuccess, clock.now()).transact(xa).unsafeRunSync()
  println(s"Created verification with id: $idCreated. Loading it ")
  println(verificationsRepo.findById(idCreated).transact(xa).unsafeRunSync())

  val idCreated2 = verificationsRepo.add(tommy.id, PoRVerificationStatus.PoRFailure, clock.now()).transact(xa).unsafeRunSync()
  println(s"Created verification with id: $idCreated2. Loading it")
  println(verificationsRepo.findById(idCreated2).transact(xa).unsafeRunSync())

}
