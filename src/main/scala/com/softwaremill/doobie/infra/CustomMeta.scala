package com.softwaremill.doobie.infra

import java.time.{ Instant, ZoneId, ZonedDateTime }
import java.util.UUID

import com.softwaremill.doobie.Verifications.VerificationsRow
import com.softwaremill.doobie.model._
import doobie.util.composite.Composite
import doobie.util.meta.Meta

trait CustomMeta {

  implicit val idMeta: Meta[UUID] = doobie.postgres.implicits.UuidType

  implicit val zonedDateTimeMeta: Meta[ZonedDateTime] = Meta[Instant].xmap(ZonedDateTime.ofInstant(_, ZoneId.of("UTC")), _.toInstant)

  implicit val verifiationStatusComposite: Composite[VerificationStatus] = Composite[(String, String)].imap({
    case (status, "ID")  => IdVerificationStatus.of(status): VerificationStatus
    case (status, "POR") => PoRVerificationStatus.of(status): VerificationStatus
  }) {
    case s: IdVerificationStatus  => (s.value, "ID")
    case s: PoRVerificationStatus => (s.value, "POR")
  }

  implicit val verifiationComposite: Composite[Verification[_]] = Composite[VerificationsRow].imap({
    case (id, userId, status: IdVerificationStatus, createdAt) =>
      IdVerification(id, userId, status, createdAt): Verification[_]
    case (id, userId, status: PoRVerificationStatus, createdAt) =>
      PoRVerification(id, userId, status, createdAt): Verification[_]
  }) {
    case a: IdVerification  => IdVerification.unapply(a).get
    case a: PoRVerification => PoRVerification.unapply(a).get
  }

}
