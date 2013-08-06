package test.com.azaptree.logging

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import com.azaptree.logging.Slf4jLogger
import com.azaptree.logging.Event
import com.azaptree.logging.EventLevel

class EventTest extends FunSuite with ShouldMatchers with Slf4jLogger {

  test("Create 1000 Events only specifying required params") {

    val start = System.currentTimeMillis()
    for (i <- 1 to 1000) {
      val event = Event(
        namespace = "com.azaptree.security",
        name = "subject.authc.success",
        level = EventLevel.INFO,
        source = "EventTest")
    }
    info(s"time to create 1000 events ${System.currentTimeMillis() - start} msec")

    var events = Set.empty[Event]
    for (i <- 1 to 1000) {
      val event = Event(
        namespace = "com.azaptree.security",
        name = "subject.authc.success",
        level = EventLevel.INFO,
        source = "EventTest")

      events += event
    }

    assert(events.size == 1000)
  }

}