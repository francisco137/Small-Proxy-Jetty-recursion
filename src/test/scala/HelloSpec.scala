
import org.scalatest._

class HelloSpec extends FunSuite with DiagrammedAssertions {
    test("Small Proxy should start with S") {
        assert("Small Proxy".startsWith("S"))
    }
}
