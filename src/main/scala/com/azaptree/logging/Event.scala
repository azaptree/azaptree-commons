package com.azaptree.logging

import com.azaptree.utils.GUID

import com.azaptree.utils._

case class Event(
  namespace: String,
  name: String,
  level: EventLevel,
  source: String,
  processInfo: ProcessInfo = ProcessInfo(),
  message: Option[String] = None,
  exceptionInfo: Option[ExceptionInfo] = None,
  eventId: GUID = GUID(),
  parentEventId: Option[GUID] = None,
  createdOnTimeMillis: Long = System.currentTimeMillis,
  attributes: Option[Map[String, AnyVal]] = None)

sealed trait EventLevel

object EventLevel {
  case object DEBUG extends EventLevel
  case object INFO extends EventLevel
  case object WARN extends EventLevel
  case object ERROR extends EventLevel
}

case class ProcessInfo(
  host: String = HOST,
  pid: Long = PID,
  thread: String = Thread.currentThread().getName(),
  stackTrace: Option[Iterable[StackTraceElement]] = None)

case class ExceptionInfo(className: String, message: String, stackTrace: String)
