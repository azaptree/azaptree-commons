package com.azaptree.utils

import java.util.UUID

import scala.language.implicitConversions

object GUID {

  def apply(guid: String) = {
    new GUID(guid)
  }

  def apply(uuid: UUID = UUID.randomUUID()) = {
    new GUID(uuid.toString().replaceAll("-", ""))
  }

  def apply() = {
    new GUID()
  }

  implicit def guid2uuid(guid: GUID): UUID = {
    val sb = new StringBuilder(36)
    val guidValue = guid.guid
    sb.append(guidValue.substring(0, 8)).append('-')
      .append(guidValue.substring(8, 12)).append('-')
      .append(guidValue.substring(12, 16)).append('-')
      .append(guidValue.substring(16, 20)).append('-')
      .append(guidValue.substring(20)).append('-')

    UUID.fromString(sb.toString())
  }

  implicit def uuid2guid(uuid: UUID): GUID = apply(uuid)
}

/**
 * Space optimized guid which strips out  "-"
 */
class GUID(val guid: String = UUID.randomUUID().toString().replaceAll("-", "")) {
  require(guid != null && guid.trim().length() == 32)

  override def toString() = guid

  override def hashCode() = guid.hashCode()

  override def equals(obj: Any) = {
    obj match {
      case that: GUID =>
        if (GUID.this eq that) {
          true
        } else {
          guid == that.guid
        }
      case _ => false
    }
  }

}