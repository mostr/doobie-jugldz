package com.softwaremill.doobie.model

import java.time.LocalDate

import com.softwaremill.doobie.sample.model.Id

case class User(id: Id, email: String, pwdHash: String, referralCode: Option[String], dateOfBirth: LocalDate)
