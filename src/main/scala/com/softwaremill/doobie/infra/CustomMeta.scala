package com.softwaremill.doobie.infra

import java.time.{ Instant, ZoneId, ZonedDateTime }
import java.util.UUID

import com.softwaremill.doobie.model.{ IdVerificationStatus, PoRVerificationStatus, VerificationStatus }
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

}
