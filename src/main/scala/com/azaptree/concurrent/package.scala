package com.azaptree

import scala.concurrent.Lock
package object concurrent {

  def withLock[A](lock: Lock)(work: () => A): A = {
    lock.acquire()
    try {
      work()
    } finally {
      lock.release()
    }
  }
}