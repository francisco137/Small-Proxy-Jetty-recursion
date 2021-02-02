
import org.scalatest._
import pl.sao.smallproxyjetty.schema.Contributor

class ContributorSpec extends FunSuite with DiagrammedAssertions {
    test("Contributor - testing the fields values reading") {
        val testContributor = Contributor(
            "pengwynn",
            865,
            "MDQ6VXNlcjg2NQ==",
            "https://avatars2.githubusercontent.com/u/865?v=4",
            "",
            "https://api.github.com/users/pengwynn",
            "https://github.com/pengwynn",
            "https://api.github.com/users/pengwynn/followers",
            "https://api.github.com/users/pengwynn/following{/other_user}",
            "https://api.github.com/users/pengwynn/gists{/gist_id}",
            "https://api.github.com/users/pengwynn/starred{/owner}{/repo}",
            "https://api.github.com/users/pengwynn/subscriptions",
            "https://api.github.com/users/pengwynn/orgs",
            "https://api.github.com/users/pengwynn/repos",
            "https://api.github.com/users/pengwynn/events{/privacy}",
            "https://api.github.com/users/pengwynn/received_events",
            "User",
            false,
            897
        )
        assert( testContributor.login               == "pengwynn"                                                     )
        assert( testContributor.id                  ==  865                                                           )
        assert( testContributor.node_id             == "MDQ6VXNlcjg2NQ=="                                             )
        assert( testContributor.avatar_url          == "https://avatars2.githubusercontent.com/u/865?v=4"             )
        assert( testContributor.gravatar_id         == ""                                                             )
        assert( testContributor.url                 == "https://api.github.com/users/pengwynn"                        )
        assert( testContributor.html_url            == "https://github.com/pengwynn"                                  )
        assert( testContributor.followers_url       == "https://api.github.com/users/pengwynn/followers"              )
        assert( testContributor.following_url       == "https://api.github.com/users/pengwynn/following{/other_user}" )
        assert( testContributor.gists_url           == "https://api.github.com/users/pengwynn/gists{/gist_id}"        )
        assert( testContributor.starred_url         == "https://api.github.com/users/pengwynn/starred{/owner}{/repo}" )
        assert( testContributor.subscriptions_url   == "https://api.github.com/users/pengwynn/subscriptions"          )
        assert( testContributor.organizations_url   == "https://api.github.com/users/pengwynn/orgs"                   )
        assert( testContributor.repos_url           == "https://api.github.com/users/pengwynn/repos"                  )
        assert( testContributor.events_url          == "https://api.github.com/users/pengwynn/events{/privacy}"       )
        assert( testContributor.received_events_url == "https://api.github.com/users/pengwynn/received_events"        )
        assert( testContributor.`type`              == "User"                                                         )
        assert( testContributor.site_admin          ==  false                                                         )
        assert( testContributor.contributions       ==  897                                                           )
    }
}
