package com.softwaremill.doobie

import java.time.ZonedDateTime

import com.softwaremill.doobie.infra.CustomMeta
import com.softwaremill.doobie.model._
import com.softwaremill.doobie.sample.model.Id
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.util.update.Update

class Verifications extends CustomMeta {

  def add(userId: Id, status: VerificationStatus, createdAt: ZonedDateTime): ConnectionIO[Long] = {
    Update[(Id, VerificationStatus, ZonedDateTime)](
      "insert into verifications (user_id, status, verification_type, created_at) values (?, ?, ?, ?)")
      .withUniqueGeneratedKeys[Long]("id")((userId, status, createdAt))
  }

  def findById(verificationId: Long): ConnectionIO[Option[Verification[_]]] = {
    sql"select * from verifications where id = $verificationId"
      .query[Verification[_]]
      .option
  }

  def findVerificationStats(): ConnectionIO[List[UserVerificationStats]] = {
    sql"select u.id, v.status, v.verification_type, count(*) as count from verifications v join users u on v.user_id = u.id group by u.id, v.verification_type, v.status"
      .query[(Id, VerificationStatus, Long)]
      .to[List]
      .map { rows =>
        rows
          .groupBy(_._1)
          .map { case (k, v) => UserVerificationStats(k, v.map(s => (s._2, s._3)).toMap) }
          .toList
      }
  }

}

object Verifications {
  type VerificationsRow = (Long, Id, VerificationStatus, ZonedDateTime)
}
