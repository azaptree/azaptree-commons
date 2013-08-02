package test.com.azaptree.utils

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import com.azaptree.logging.Slf4jLogger
import java.util.UUID
import com.azaptree.utils.GUID
import org.apache.commons.codec.binary.Base64

class GUIDSpec extends FunSuite with ShouldMatchers with Slf4jLogger {

  test("compare performance between UUID and GUID") {
    var guids = Vector.empty[GUID]
    var start = System.currentTimeMillis()
    val count = 100000
    for (i <- 1 to count) {
      val guid = GUID()
      GUID(guid.guid)
    }
    log.info(s"creating $count GUID time = {}", System.currentTimeMillis() - start)

    start = System.currentTimeMillis()
    for (i <- 1 to count) {
      val uuid = UUID.randomUUID()
      UUID.fromString(uuid.toString())
    }
    log.info(s"creating $count UUID time = {}", System.currentTimeMillis() - start)
  }

  test("A GUID created from another GUID's guid are equal") {
    val guid1 = GUID()
    val guid2 = GUID(guid1.guid)

    assert(guid1 == guid2)
    assert(guid1.## == guid2.##)
  }

  test("GUIDs are unique") {
    var guids = Set.empty[GUID]
    val count = 100000
    for (i <- 1 to count) {
      guids += GUID()
    }

    assert(guids.size == count)

  }

  test("UTF8 bytes length vs String") {
    val utf8Bytes = GUID().guid.getBytes("UTF-8")
    val guidBytes = GUID().guid.getBytes()

    log.info("utf8Bytes.length = {}, guidBytes.length = {}", utf8Bytes.length, guidBytes.length)

    val base64GUID = Base64.encodeBase64(utf8Bytes)
    log.info("base64GUID.length = {}", base64GUID.length)
    if (base64GUID.length > utf8Bytes.length) {
      info("Base64 encoded guid is longer than plain guid")
    } else {
      info("Base64 encoded guid is shorter than plain guid")
    }
  }

  test("GUID to UUID conversion") {
    val uuid: UUID = GUID()
    val guid: GUID = UUID.randomUUID()

    val uuid2: UUID = guid
    val guid2: GUID = uuid

    log.info("uuid = {}", uuid)
    log.info("guid = {}", guid)
    log.info("uuid2 = {}", uuid2)
    log.info("guid2 = {}", guid2)
  }

}