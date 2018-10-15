package com.softwaremill.doobie.model

sealed trait VerificationStatus {
  val value: String
}

sealed trait IdVerificationStatus  extends VerificationStatus
sealed trait PoRVerificationStatus extends VerificationStatus

object IdVerificationStatus {

  case object IdSuccess extends IdVerificationStatus {
    val value = "SUCCESS"
  }

  case object IdFailure extends IdVerificationStatus {
    val value = "FAILURE"
  }

  private val all = List(IdSuccess, IdFailure)

  def of(value: String): IdVerificationStatus =
    all.find(_.value == value).getOrElse(throw new IllegalArgumentException(s"Unknown status $value"))

}
object PoRVerificationStatus {
  case object PoRSuccess extends PoRVerificationStatus {
    val value = "SUCCESS"
  }
  case object PoRFailure extends PoRVerificationStatus {
    val value = "FAILURE"
  }
  case object PoRExpired extends PoRVerificationStatus {
    val value = "EXPIRED"
  }

  private val all = List(PoRSuccess, PoRFailure, PoRExpired)

  def of(value: String): PoRVerificationStatus =
    all.find(_.value == value).getOrElse(throw new IllegalArgumentException(s"Unknown status $value"))
}
