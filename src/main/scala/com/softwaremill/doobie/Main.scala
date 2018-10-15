package com.softwaremill.doobie

import java.time.LocalDate

import cats.effect.IO
import com.softwaremill.doobie.infra.{ Clock, Database, IdGen, UTCClock }
import com.softwaremill.doobie.model.IdVerificationStatus.{ IdFailure, IdSuccess }
import com.softwaremill.doobie.model.PoRVerificationStatus.{ PoRExpired, PoRFailure, PoRSuccess }
import com.softwaremill.doobie.model.{ NewVerificationData, User }
import doobie.free.connection.ConnectionIO
import doobie.implicits._

object Main extends App {

  val usersRepo         = new Users
  val verificationsRepo = new Verifications
  val idGen: IdGen      = new IdGen
  val clock: Clock      = UTCClock
  val xa                = Database.connect[IO]()

  val tommy                   = User(idGen.newId(), "tommy@example.com", "supersecret", None, LocalDate.of(1982, 8, 19))
  val cio: ConnectionIO[Unit] = usersRepo.add(tommy)
  val eff: IO[Unit]           = cio.transact(xa)
  // finally run it
  eff.unsafeRunSync()

  List(
    NewVerificationData(tommy.id, IdSuccess, clock.now()),
    NewVerificationData(tommy.id, IdFailure, clock.now()),
    NewVerificationData(tommy.id, IdFailure, clock.now()),
    NewVerificationData(tommy.id, PoRFailure, clock.now()),
    NewVerificationData(tommy.id, PoRSuccess, clock.now()),
    NewVerificationData(tommy.id, PoRExpired, clock.now()),
    NewVerificationData(tommy.id, PoRExpired, clock.now())
  ).foreach(a => verificationsRepo.add(a.userId, a.status, a.createdAt).transact(xa).unsafeRunSync())

  println(verificationsRepo.findVerificationStats().transact(xa).unsafeRunSync())

}
