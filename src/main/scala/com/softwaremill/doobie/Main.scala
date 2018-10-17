package com.softwaremill.doobie

import java.time.LocalDate

import cats.effect.IO
import cats.implicits._
import com.softwaremill.doobie.infra.{ Clock, Database, IdGen, UTCClock }
import com.softwaremill.doobie.model.IdVerificationStatus.{ IdFailure, IdSuccess }
import com.softwaremill.doobie.model.PoRVerificationStatus.{ PoRExpired, PoRSuccess }
import com.softwaremill.doobie.model.{ NewVerificationData, User }
import doobie.free.connection
import doobie.implicits._

object Main extends App {

  val usersRepo         = new Users
  val verificationsRepo = new Verifications
  val idGen: IdGen      = new IdGen
  val clock: Clock      = UTCClock
  val xa                = Database.connect[IO]()

  val tommy = User(idGen.newId(), "tommy@example.com", "supersecret", None, LocalDate.of(1982, 8, 19))

  val verifications = List(
    NewVerificationData(tommy.id, IdSuccess, clock.now()),
    NewVerificationData(tommy.id, PoRExpired, clock.now()),
    NewVerificationData(tommy.id, PoRSuccess, clock.now()),
    NewVerificationData(tommy.id, PoRExpired, clock.now()),
    NewVerificationData(tommy.id, IdFailure, clock.now())
  )
  val addTommy         = usersRepo.add(tommy)
  val addVerifications = verifications.map(verificationsRepo.add).sequence

  val transaction = addTommy.flatMap { _ =>
    if (tommy.email.endsWith("pl")) {
      println("Rolling back")
      connection.rollback
    } else {
      addVerifications.map(_ => ())
    }
  }

  // run it
  transaction.transact(xa).unsafeRunSync()

}
