package com.softwaremill.doobie.infra

import java.net.URI

final case class DBConfig(user: String, pass: String, dbUrl: String)

private[infra] object DBConfig {
  def apply(jdbcUrl: String): DBConfig = {
    val parsed = new URI(jdbcUrl)
    val dbUrl  = s"jdbc:postgresql://${parsed.getHost}:${parsed.getPort}${parsed.getPath}"
    val userInfo = Option(parsed.getUserInfo)
      .map(userInfo => userInfo.split(":", 2))
      .getOrElse(throw new RuntimeException("No user info found in DB url"))
    DBConfig(userInfo(0), userInfo(1), dbUrl)
  }
}
