package com.softwaremill.doobie

import java.time.ZonedDateTime

import com.softwaremill.doobie.infra.CustomMeta
import com.softwaremill.doobie.model._
import com.softwaremill.doobie.sample.model.Id
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.util.update.Update

class Verifications extends CustomMeta {

  private type VerificationsRow = (Long, Id, VerificationStatus, ZonedDateTime)

  def add(userId: Id, status: VerificationStatus, createdAt: ZonedDateTime): ConnectionIO[Long] = {
    Update[(Id, VerificationStatus, ZonedDateTime)](
      "insert into verifications (user_id, status, verification_type, created_at) values (?, ?, ?, ?)")
      .withUniqueGeneratedKeys[Long]("id")((userId, status, createdAt))
  }

  def findById(verificationId: Long): ConnectionIO[Option[Verification[_]]] = {
    sql"select * from verifications where id = $verificationId"
      .query[VerificationsRow]
      .option
      .map(_.map {
        case (id, userId, status: IdVerificationStatus, createdAt)  => IdVerification(id, userId, status, createdAt)
        case (id, userId, status: PoRVerificationStatus, createdAt) => PoRVerification(id, userId, status, createdAt)
      })
  }

}
