package com.azaptree.utils

import java.util.UUID

object GUID {

  def apply(guid: String) = {
    new GUID(guid)
  }

  def apply() = {
    new GUID()
  }
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