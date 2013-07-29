package com.azaptree.concurrent

import java.util.concurrent.Callable
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.RunnableScheduledFuture
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit._
import java.util.concurrent.ScheduledExecutorService

sealed trait TaskSchedule

case class OneTimeTaskSchedule(delay: Long = 0, timeUnit: TimeUnit = SECONDS) extends TaskSchedule {
  require(delay >= 0, "delay must be >= 0")
}

case class PeriodicTaskSchedule(initialDelay: Long = 0, period: Long, timeUnit: TimeUnit = SECONDS) extends TaskSchedule {
  require(initialDelay >= 0, "initialDelay must be >= 0")
  require(period > 0, "period must be > 0")
}

case class RecurringTaskWithFixedDelayTaskSchedule(initialDelay: Long = 0, delay: Long, timeUnit: TimeUnit = SECONDS) extends TaskSchedule {
  require(initialDelay >= 0, "initialDelay must be >= 0")
  require(delay > 0, "delay must be > 0")
}

sealed trait ScheduledTask {
  def schedule(executor: ScheduledExecutorService): ScheduledFuture[_]
}

case class OneTimeTask[A](schedule: OneTimeTaskSchedule)(task: () => A) extends ScheduledTask {
  override def schedule(executor: ScheduledExecutorService): ScheduledFuture[A] = {
    val callable: Callable[A] = new Callable[A]() {
      override def call(): A = task()
    }
    executor.schedule(callable, schedule.delay, schedule.timeUnit)
  }
}

/**
 * Creates and executes a periodic action that becomes enabled first after the given initial delay, and subsequently with the given period.
 * That is, executions will commence after initialDelay then initialDelay+period, then initialDelay + 2 * period, and so on.
 */
case class PeriodicTask(schedule: PeriodicTaskSchedule)(task: () => Unit) extends ScheduledTask {
  override def schedule(executor: ScheduledExecutorService): ScheduledFuture[_] = {
    val runnable: Runnable = new Runnable() {
      override def run() = task()
    }
    executor.scheduleAtFixedRate(runnable, schedule.initialDelay, schedule.period, schedule.timeUnit)
  }
}

/**
 * Creates and executes a periodic action that becomes enabled first after the given initial delay, and
 * subsequently with the given delay between the termination of one execution and the commencement of the next.
 */
case class RecurringTaskWithFixedDelay(schedule: RecurringTaskWithFixedDelayTaskSchedule)(task: () => Unit) extends ScheduledTask {
  override def schedule(executor: ScheduledExecutorService): ScheduledFuture[_] = {
    val runnable: Runnable = new Runnable() {
      override def run() = task()
    }
    executor.scheduleWithFixedDelay(runnable, schedule.initialDelay, schedule.delay, schedule.timeUnit)
  }
}

