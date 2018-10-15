package com.softwaremill.doobie.infra

import java.util.UUID

import doobie.util.meta.Meta

trait CustomMeta {

  implicit val idMeta: Meta[UUID] = doobie.postgres.implicits.UuidType

}
