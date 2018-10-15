package com.softwaremill.doobie.infra

import java.util.UUID

import com.softwaremill.doobie.sample.model.Id

class IdGen {
  def newId(): Id = UUID.randomUUID()
}
