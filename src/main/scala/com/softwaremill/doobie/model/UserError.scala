package com.softwaremill.doobie.model

sealed trait UserError

case object UserAlreadyExists extends UserError
