package com.softwaremill.doobie

import java.time.LocalDate

import cats.effect.IO
import cats.implicits._
import com.softwaremill.doobie.infra.{ Clock, Database, IdGen, UTCClock }
import com.softwaremill.doobie.model.IdVerificationStatus.IdSuccess
import com.softwaremill.doobie.model.PoRVerificationStatus.PoRExpired
import com.softwaremill.doobie.model.{ NewVerificationData, User }
import doobie.free.connection.ConnectionIO
import doobie.implicits._

object Main extends App {

  val usersRepo         = new Users
  val verificationsRepo = new Verifications
  val idGen: IdGen      = new IdGen
  val clock: Clock      = UTCClock
  val xa                = Database.connect[IO]()

  val tommy               = User(idGen.newId(), "tommy@example.com", "supersecret", None, LocalDate.of(1982, 8, 19))
  val validVerification   = NewVerificationData(tommy.id, IdSuccess, clock.now())
  val invalidVerification = NewVerificationData(idGen.newId(), PoRExpired, clock.now()) // unknown user id

  val addTommy         = usersRepo.add(tommy)
  val addVerifications = List(validVerification, invalidVerification).map(verificationsRepo.add).sequence

  val transaction: ConnectionIO[Unit] = for {
    _ <- addTommy
    _ <- addVerifications
  } yield {
    println("All good")
  }

  transaction
    .transact(xa)
    .attempt
    .map(_.left.map { err =>
      println("Ooopsie, got error")
      err.printStackTrace()
    })
    .unsafeRunSync()

}
