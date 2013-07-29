package test.com.azaptree.concurrent

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import com.azaptree.concurrent.OneTimeTask
import com.azaptree.concurrent.OneTimeTaskSchedule
import java.util.concurrent.Executors
import org.scalatest.BeforeAndAfterAll
import com.azaptree.concurrent.PeriodicTaskSchedule
import com.azaptree.concurrent.PeriodicTask
import com.azaptree.concurrent.RecurringTaskWithFixedDelay
import com.azaptree.concurrent.RecurringTaskWithFixedDelayTaskSchedule

class ScheduledTasksSpec extends FunSuite with ShouldMatchers with BeforeAndAfterAll {

  val executor = Executors.newScheduledThreadPool(5)

  override protected def afterAll() = {
    executor.shutdownNow()
  }

  test("A OneTimeTask[A] can be scheduled to run with a start delay relative to now") {
    var counter = 0

    val task = OneTimeTask[Unit](OneTimeTaskSchedule(1)) { () =>
      counter += 1
    }

    for (i <- 1 to 5) task.schedule(executor)
    counter should be(0)
    Thread.sleep(1010l)

    counter should be(5)
  }

  test("A OneTimeTask[A] will validate that delay >= 0") {
    try {
      OneTimeTask[Unit](OneTimeTaskSchedule(-1)) { () => }
      throw new IllegalStateException("Expected an IllegalArgumentException to be thrown")
    } catch {
      case t: IllegalArgumentException =>
      case t: Throwable => throw new IllegalStateException("Expected an IllegalArgumentException to be thrown")
    }
  }

  test("A PeriodicTask can be scheduled to run on a periodic schedule between successive executions with an initial start delay relative to now") {
    var counter = 0

    val task = PeriodicTask(PeriodicTaskSchedule(period = 1)) { () =>
      counter += 1
    }

    task.schedule(executor)
    Thread.sleep(1010l)
    counter should be(2)
  }

  test("A PeriodicTaskSchedule will validate that initialDelay >= 0") {
    try {
      PeriodicTaskSchedule(initialDelay = -1, period = 1)
      throw new IllegalStateException("Expected an IllegalArgumentException to be thrown")
    } catch {
      case t: IllegalArgumentException =>
      case t: Throwable => throw new IllegalStateException("Expected an IllegalArgumentException to be thrown")
    }
  }

  test("A PeriodicTaskSchedule will validate that period > 0") {
    try {
      PeriodicTaskSchedule(period = 0)
      throw new IllegalStateException("Expected an IllegalArgumentException to be thrown")
    } catch {
      case t: IllegalArgumentException =>
      case t: Throwable => throw new IllegalStateException("Expected an IllegalArgumentException to be thrown")
    }
  }

  test("A RecurringTaskWithFixedDelay can be scheduled to run with a delay between the termination of one execution and the commencement of the next and a initial start delay") {
    var counter = 0

    val task = RecurringTaskWithFixedDelay(RecurringTaskWithFixedDelayTaskSchedule(delay = 1)) { () =>
      counter += 1
    }

    task.schedule(executor)
    Thread.sleep(1010l)
    counter should be(2)
  }

  test("A RecurringTaskWithFixedDelay will validate that initialDelay >= 0") {
    try {
      RecurringTaskWithFixedDelayTaskSchedule(initialDelay = -1, delay = 1)
      throw new IllegalStateException("Expected an IllegalArgumentException to be thrown")
    } catch {
      case t: IllegalArgumentException =>
      case t: Throwable => throw new IllegalStateException("Expected an IllegalArgumentException to be thrown")
    }
  }

  test("A RecurringTaskWithFixedDelay will validate that delay > 0") {
    try {
      RecurringTaskWithFixedDelayTaskSchedule(delay = 0)
      throw new IllegalStateException("Expected an IllegalArgumentException to be thrown")
    } catch {
      case t: IllegalArgumentException =>
      case t: Throwable => throw new IllegalStateException("Expected an IllegalArgumentException to be thrown")
    }
  }

}