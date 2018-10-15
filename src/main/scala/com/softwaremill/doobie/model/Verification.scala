package com.softwaremill.doobie.model

import java.time.ZonedDateTime

import com.softwaremill.doobie.sample.model.Id

sealed trait Verification[T <: VerificationStatus] {
  val id: Long
  val userId: Id
  val status: T
  val createdAt: ZonedDateTime
}

case class IdVerification(id: Long, userId: Id, status: IdVerificationStatus, createdAt: ZonedDateTime)
    extends Verification[IdVerificationStatus]

case class PoRVerification(id: Long, userId: Id, status: PoRVerificationStatus, createdAt: ZonedDateTime)
    extends Verification[PoRVerificationStatus]
