package com.softwaremill.doobie.infra

import java.time.{ Instant, ZoneId, ZonedDateTime }
import java.util.UUID

import doobie.util.meta.Meta

trait CustomMeta {

  implicit val idMeta: Meta[UUID] = doobie.postgres.implicits.UuidType

  implicit val zonedDateTimeMeta: Meta[ZonedDateTime] = Meta[Instant].xmap(ZonedDateTime.ofInstant(_, ZoneId.of("UTC")), _.toInstant)

}
