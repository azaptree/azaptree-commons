package com.azaptree.entity

import java.util.UUID
import java.util.Objects
import com.azaptree.utils.GUID

/**
 * Used to track entity versions.
 *
 */
class VersionedEntity[+A](
    val entityId: GUID = GUID(),
    val createdOn: Long = System.currentTimeMillis(),
    val createdBy: Option[UUID] = None,
    val updatedOn: Long = System.currentTimeMillis(),
    val updatedBy: Option[UUID] = None,
    val entityVersion: Long = 0l,
    val entity: A) extends Serializable with Equals {

  require(entityId != null, "entityId is required")
  require(entity != null, "entity is required")

  override def canEqual(that: Any) = that.isInstanceOf[VersionedEntity[A]]

  override def equals(obj: Any): Boolean = {
    obj match {
      case that: VersionedEntity[A] =>
        if (this eq that) {
          true
        } else {
          (that canEqual this) && (entityId == that.entityId) && (entityVersion == that.entityVersion)
        }
      case _ => false
    }
  }

  override def hashCode(): Int = Objects.hash(entityId, entityVersion.toString)

  /**
   * returns a new version with a new entityVersion and sets the entityUpdatedOn to the current timestamp set to the new entity
   */
  def newVersion[B >: A](newEntity: B, updatedBy: Option[UUID] = None): VersionedEntity[B] = {
    require(newEntity != null)
    new VersionedEntity[B](
      entityId = this.entityId,
      updatedOn = System.currentTimeMillis(),
      updatedBy = updatedBy,
      entityVersion = entityVersion + 1,
      entity = newEntity)
  }
}

