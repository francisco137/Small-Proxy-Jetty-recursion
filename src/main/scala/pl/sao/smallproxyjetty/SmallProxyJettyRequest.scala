package pl.sao.smallproxyjetty

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.{ActionResult, BadRequest, Ok, ScalatraServlet, Unauthorized}
import org.scalatra.json.JacksonJsonSupport
import pl.sao.smallproxyjetty.schema.{Contributor, Person, Repository}
import play.api.libs.json.{JsObject, Json}

import scala.annotation.tailrec

class SmallProxyJettyRequest extends ScalatraServlet with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats

  val hint: JsObject = Json.obj(
    "Form of the legal path" -> "/org/:organization/contributors",
    "Allowed characters in path" -> "[a-zA-Z0-9_,-]",
    "Start variables" -> Json.obj(
      "GH_TOKEN" -> "Github authorization token as env variable - mandatory",
      "GH_PER_PAGE" -> "Github per_page get argument - default 50"
    )
  )

  val getBadRequest: ActionResult = {
    BadRequest(Json.obj(
      "message" -> s"Illegal path!",
      "hint" -> hint
    ))
  }

  val getGeneralInformation: ActionResult = {
    Ok(Json.obj(
      "message" -> "General Information",
      "hint" -> hint
    ))
  }

  val noTokenWarning: ActionResult = {
    Unauthorized(Json.obj(
      "message" -> "No authorization token!",
      "hint" -> hint
    ))
  }


  //--------------------------------------------------------------------------------

  // GH_TOKEN has to be the GitHub authorization token written as environment variable:
  private[this] val gh_token: String = sys.env.getOrElse("GH_TOKEN", "")
  // GH_GH_PER_PAGE - how many records per one request:
  private[this] val per_page: String = sys.env.getOrElse("GH_PER_PAGE", "50")

  case class ResponseData[A](status: Int, code: String, data: List[A])

  @tailrec
  private[this] def getData[A: Manifest](acc: List[A], url: String, page: Int = 1): ResponseData[A] = {
    val response = requests.get(
      url + s"?page=$page&per_page=$per_page",
      headers = Map("Authorization" -> s"token $gh_token"),
      check = false)

    response.statusCode match {
      case 200 => {
        val objects_list = parse(response.text).extract[List[A]]
        if (objects_list.isEmpty) {
          ResponseData(response.statusCode, response.statusMessage, acc)
        }
        else {
          getData(acc ::: objects_list, url, page + 1)
        }
      }
      case _ => ResponseData(response.statusCode, response.statusMessage, acc)
    }
  }

  def getContributors(org: String, asc_desc: String = "asc_desc"): ActionResult = {

    val result = getData(List[Repository](), s"https://api.github.com/orgs/$org/repos")
    result.status match {
      case 200 => {
        val repositories = result.data
        if (repositories.isEmpty) {
          ActionResult(404, Json.obj("message" -> s"Repositories for organization '$org' not found!"), Map.empty)
        } else {
          val contributors = repositories
            .map(x => x.name)
            .map(repo => {
              val result = getData(List[Contributor](), s"https://api.github.com/repos/$org/$repo/contributors")

              // if during collecting data, error appears, then we immediately return this status
              if ( result.status != 200 )
                return ActionResult(result.status, Json.obj("message" -> result.code), Map.empty)

              result.data
                .map(x => Person(x.login, x.contributions))
            }).reduce(_ ::: _)

          contributors match {
            case Nil => ActionResult(404, Json.obj("message" -> s"Contributors to the repositories of '$org' not found!"), Map.empty)
            case _ => {
              val contributorsList = contributors
                .groupBy(_.name)
                .map(x => Person(x._1, x._2.map(_.total).sum))
                .toList
                .sortWith(Person.personSort)
                .map(x => Json.obj(x.name -> x.total))

              ActionResult(200, Json.arr(contributorsList), Map.empty)
            }
          }
        }
      }
      case _ => ActionResult(result.status, Json.obj("message" -> result.code), Map.empty)
    }
  }
}