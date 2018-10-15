package com.softwaremill.doobie

import java.time.ZonedDateTime

import com.softwaremill.doobie.infra.CustomMeta
import com.softwaremill.doobie.model._
import com.softwaremill.doobie.sample.model.Id
import doobie.free.connection.ConnectionIO
import doobie.implicits._

class Verifications extends CustomMeta {

  private type VerificationsRow = (Long, Id, String, String, ZonedDateTime)

  private val IdVerificationType = "ID"

  private val PoRVerificationType = "POR"

  def add(userId: Id, status: VerificationStatus, createdAt: ZonedDateTime): ConnectionIO[Long] = {
    val verificationType = status match {
      case _: IdVerificationStatus  => IdVerificationType
      case _: PoRVerificationStatus => PoRVerificationType
    }
    sql"insert into verifications (user_id, status, verification_type, created_at) values ($userId, ${status.value}, $verificationType, $createdAt)".update
      .withUniqueGeneratedKeys[Long]("id")
  }

  def findById(verificationId: Long): ConnectionIO[Option[Verification[_]]] = {
    sql"select * from verifications where id = $verificationId"
      .query[VerificationsRow]
      .option
      .map(_.map {
        case (id, userId, status, IdVerificationType, createdAt) =>
          IdVerification(id, userId, IdVerificationStatus.of(status), createdAt)
        case (id, userId, status, PoRVerificationType, createdAt) =>
          PoRVerification(id, userId, PoRVerificationStatus.of(status), createdAt)
        case unknown =>
          throw new IllegalStateException(s"Cannot build verification record out of $unknown")
      })
  }

}
