package com.softwaremill.doobie.infra

import cats.effect.Async
import doobie.util.transactor.Transactor
import org.flywaydb.core.Flyway

object Database {
  def connect[M[_]: Async](url: String = "postgres://doobie:doobie123@localhost:5432/doobie"): Transactor[M] = {
    val config = DBConfig(url)
    val flyway = new Flyway()
    flyway.setDataSource(config.dbUrl, config.user, config.pass)
    flyway.clean()
    flyway.migrate()
    Transactor.fromDataSource[M](flyway.getDataSource)
  }
}
