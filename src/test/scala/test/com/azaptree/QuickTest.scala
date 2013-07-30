package test.com.azaptree

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import com.azaptree.logging.Slf4jLogger
import java.util.UUID
import org.apache.commons.codec.binary.Base64
import com.azaptree.utils.GUID

class QuickTest extends FunSuite with ShouldMatchers with Slf4jLogger {

  test("Base64 encoding UUID vs UUID.toString") {
    val uuid = UUID.randomUUID()
    val uuidStr = uuid.toString();
    log.info("uuid string : {} : {}", uuidStr.length(), uuidStr)
    log.info("{} : {}", uuid.getMostSignificantBits(), uuid.getLeastSignificantBits())
  }

}