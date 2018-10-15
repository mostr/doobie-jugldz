package com.softwaremill.doobie.model

import java.time.ZonedDateTime

import com.softwaremill.doobie.sample.model.Id

case class NewVerificationData(userId: Id, status: VerificationStatus, createdAt: ZonedDateTime)
