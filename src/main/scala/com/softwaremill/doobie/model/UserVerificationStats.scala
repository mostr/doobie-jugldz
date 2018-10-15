package com.softwaremill.doobie.model

import com.softwaremill.doobie.sample.model.Id

case class UserVerificationStats(userId: Id, verifications: Map[VerificationStatus, Long])
