import pl.sao.smallproxyjetty._
import javax.servlet.ServletContext
import org.scalatra.LifeCycle

class ScalatraBootstrap extends LifeCycle {

	override def init(context: ServletContext) {
		try {
			context.mount(new SmallProxyJetty, "/*")
		} catch {
			case e: Throwable => e.printStackTrace()
		}
	}
}
