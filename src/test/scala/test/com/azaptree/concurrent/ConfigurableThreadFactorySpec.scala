package test.com.azaptree.concurrent

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import com.azaptree.concurrent.ConfigurableThreadFactory
import java.util.concurrent.CountDownLatch
import scala.util.Try

class ConfigurableThreadFactorySpec extends FunSpec with ShouldMatchers {

  describe("A ConfigurableThreadFactory") {
    it("can be used to create threads that start with a specified base name") {
      var threadName: String = ""
      var daemon: Boolean = false
      var priority: Int = -99

      var countDownLatch = new CountDownLatch(1)

      val tf1 = ConfigurableThreadFactory(threadBaseName = Some("azaptree"))
      val t1 = tf1.newThread(new Runnable() {
        def run() = {
          threadName = Thread.currentThread().getName()
          daemon = Thread.currentThread().isDaemon()
          priority = Thread.currentThread().getPriority()
          countDownLatch.countDown()
        }
      })

      t1.start()
      countDownLatch.await()
      threadName should be("azaptree-1")
      daemon should be(false)
      priority should be(Thread.NORM_PRIORITY)
    }

    it("can be used to create daemon threads") {
      var threadName: String = ""
      var daemon: Boolean = false
      var priority: Int = -99

      var countDownLatch = new CountDownLatch(1)

      val tf1 = ConfigurableThreadFactory(Some("azaptree"), Some(true))
      val t1 = tf1.newThread(new Runnable() {
        def run() = {
          threadName = Thread.currentThread().getName()
          daemon = Thread.currentThread().isDaemon()
          priority = Thread.currentThread().getPriority()
          countDownLatch.countDown()
        }
      })

      t1.start()
      countDownLatch.await()
      threadName should be("azaptree-1")
      daemon should be(true)
      priority should be(Thread.NORM_PRIORITY)
    }

    it("can be used to create threads with different priorities") {
      var threadName: String = ""
      var daemon: Boolean = false
      var priority: Int = -99

      var countDownLatch = new CountDownLatch(1)

      val tf1 = ConfigurableThreadFactory(threadBaseName = Some("azaptree"), daemon = Some(true), priority = Some(Thread.MIN_PRIORITY))
      val t1 = tf1.newThread(new Runnable() {
        def run() = {
          threadName = Thread.currentThread().getName()
          daemon = Thread.currentThread().isDaemon()
          priority = Thread.currentThread().getPriority()
          countDownLatch.countDown()
        }
      })

      t1.start()
      countDownLatch.await()
      threadName should be("azaptree-1")
      daemon should be(true)
      priority should be(Thread.MIN_PRIORITY)
    }

    it("can be used to create threads with a specific Thread.UncaughtExceptionHandler") {
      var threadName: String = ""
      var daemon: Boolean = false
      var priority: Int = -99

      var countDownLatch = new CountDownLatch(1)

      var uncaughtExceptionHandled: Boolean = false

      val uncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        override def uncaughtException(t: Thread, e: Throwable) = {
          uncaughtExceptionHandled = true
        }
      }

      val tf1 = ConfigurableThreadFactory(threadBaseName = Some("azaptree"), daemon = Some(true), priority = Some(Thread.MIN_PRIORITY), uncaughtExceptionHandler = Some(uncaughtExceptionHandler))
      val t1 = tf1.newThread(new Runnable() {
        def run() = {
          threadName = Thread.currentThread().getName()
          daemon = Thread.currentThread().isDaemon()
          priority = Thread.currentThread().getPriority()
          countDownLatch.countDown()
          throw new RuntimeException()
        }
      })

      t1.start()
      countDownLatch.await()
      threadName should be("azaptree-1")
      daemon should be(true)
      priority should be(Thread.MIN_PRIORITY)
      uncaughtExceptionHandled should be(true)
    }

    it("validates that if a thread base name is specified then it must not be blank") {
      assert(Try { ConfigurableThreadFactory(threadBaseName = Some("")) } isFailure)
      assert(Try { ConfigurableThreadFactory(threadBaseName = Some(" ")) } isFailure)
      assert(Try { ConfigurableThreadFactory(threadBaseName = Some(null)) } isFailure)
    }

    it(s"validates that if a thread priority is specified then be >= ${Thread.MIN_PRIORITY} && <= ${Thread.MAX_PRIORITY}") {
      assert(Try { ConfigurableThreadFactory(priority = Some(Thread.MAX_PRIORITY + 1)) } isFailure)
      assert(Try { ConfigurableThreadFactory(priority = Some(Thread.MIN_PRIORITY - 1)) } isFailure)
    }

    it("validates that if a custom Thread.UncaughtExceptionHandler is specified then it is not null") {
      assert(Try { ConfigurableThreadFactory(uncaughtExceptionHandler = Some(null)) } isFailure)
    }
  }
}