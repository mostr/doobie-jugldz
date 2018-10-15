package com.softwaremill.doobie.infra

import java.time.format.DateTimeFormatter
import java.time.{ Instant, ZoneId, ZoneOffset, ZonedDateTime }

trait Clock {

  val zone: ZoneOffset
  val zoneId: ZoneId
  def now(): ZonedDateTime
  def nowTimestamp(): Long

  def from(timestamp: Long): ZonedDateTime =
    ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zone)

  def from(isoOffsetDateTimeString: String): ZonedDateTime =
    ZonedDateTime.parse(isoOffsetDateTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(zoneId))
}

object UTCClock extends Clock {
  override lazy val zone: ZoneOffset = ZoneOffset.UTC
  override lazy val zoneId: ZoneId   = ZoneId.of("UTC")
  override def now(): ZonedDateTime  = ZonedDateTime.now(zoneId)
  override def nowTimestamp(): Long  = now().toInstant.toEpochMilli
}
