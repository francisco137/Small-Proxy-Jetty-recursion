package pl.sao.smallproxyjetty.schema

case class Person (
    name  : String
   ,total : Int
)

object Person {

    import scala.math.Ordering.Implicits._
    implicit val asc_desc = "desc"

    def personSort (x: Person, y: Person): Boolean = {
        if ( asc_desc == "desc" ) { x.total > y.total } else { x.total < y.total }
    }
}

case class Contributor (
    login               : String
   ,id                  : Int
   ,node_id             : String
   ,avatar_url          : String
   ,gravatar_id         : String
   ,url                 : String
   ,html_url            : String
   ,followers_url       : String
   ,following_url       : String
   ,gists_url           : String
   ,starred_url         : String
   ,subscriptions_url   : String
   ,organizations_url   : String
   ,repos_url           : String
   ,events_url          : String
   ,received_events_url : String
   ,`type`              : String
   ,site_admin          : Boolean
   ,contributions       : Int
)
