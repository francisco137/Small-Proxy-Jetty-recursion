package pl.sao.smallproxyjetty

import org.scalatra._
import org.scalatra.{AsyncResult, FutureSupport}
import pl.sao.smallproxyjetty.util.SmallProxyJettyLogger
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

class SmallProxyJetty extends ScalatraServlet with FutureSupport {

  protected implicit def executor: ExecutionContextExecutor = ExecutionContext.global
  private[this] val per_page: String = sys.env.getOrElse("GH_PER_PAGE", "50")
  private[this] val gh_token: String = sys.env.getOrElse("GH_TOKEN", "")

  before() { contentType = "application/json" }

  val performRequest = new SmallProxyJettyRequest

  get("/*") {
    new AsyncResult {
      val is: Future[ActionResult] =
        Future(performRequest.getBadRequest)
    }
  }

  get("/") {
    new AsyncResult {
      val is: Future[ActionResult] =
        Future(performRequest.getGeneralInformation)
    }
  }

  get("/notoken") {
    new AsyncResult {
      val is: Future[ActionResult] =
        Future(performRequest.noTokenWarning)
    }
  }

  get("/org/:organization/contributors") {
    new AsyncResult {
      val is: Future[ActionResult] = Future{
        if (gh_token == "") {
          performRequest.noTokenWarning
        }
        else {
          val t0 = System.nanoTime()
          val organization = params("organization")
          val contributors = performRequest.getContributors(organization)
          val t1 = System.nanoTime()
          SmallProxyJettyLogger.logger.info(s"time = ${(t1 - t0) / 1000000} ms (per_page = $per_page)")
          contributors
        }
      }
    }

  }
}
