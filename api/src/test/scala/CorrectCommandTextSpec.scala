import docker.effect.algebra._
import org.scalatest.{ Matchers, WordSpecLike }
import shapeless.::
import docker.effect.`.`

final class CorrectCommandTextSpec extends WordSpecLike with Matchers {

  "printing command" should {
    "produce the expected text" in {
      print0[docker :: images :: `.`]                                           shouldBe "docker images"
      print0[docker :: images :: all :: `.`]                                    shouldBe "docker images --all"
      print0[docker :: images :: a :: `.`]                                      shouldBe "docker images -a"
      print0[docker :: images :: q :: `.`]                                      shouldBe "docker images -q"
      print0[docker :: images :: aq :: `.`]                                     shouldBe "docker images -aq"
      print0[docker :: ps :: `.`]                                               shouldBe "docker ps"
      print0[docker :: ps :: all :: `.`]                                        shouldBe "docker ps --all"
      print0[docker :: ps :: q :: `.`]                                          shouldBe "docker ps -q"
      print0[docker :: kill :: signal :: KILL :: `.`]                           shouldBe "docker kill --signal=KILL"
      print0[docker :: kill :: s :: HUP :: `.`]                                 shouldBe "docker kill -s=HUP"
      print1[docker :: kill :: Id :: `.`](Id("fd484f19954f"))                   shouldBe "docker kill fd484f19954f"
      print1[docker :: kill :: signal :: KILL :: Id :: `.`](Id("fd484f19954f")) shouldBe "docker kill --signal=KILL fd484f19954f"
      print1[docker :: kill :: s :: HUP :: Id :: `.`](Id("fd484f19954f"))       shouldBe "docker kill -s=HUP fd484f19954f"
      print1[docker :: rmi :: Id :: `.`](Id("fd484f19954f"))                    shouldBe "docker rmi fd484f19954f"
      print1[docker :: rmi :: Repo :: `.`](Repo("test-repo"))                   shouldBe "docker rmi test-repo"
      print1[docker :: run :: Name :: `.`](Name("test-container"))              shouldBe "docker run test-container"
      print1[docker :: run :: Id :: `.`](Id("fd484f19954f"))                    shouldBe "docker run fd484f19954f"
      print1[docker :: run :: detached :: Name :: `.`](Name("test-container"))  shouldBe "docker run --detached test-container"
      print1[docker :: run :: detached :: Id :: `.`](Id("fd484f19954f"))        shouldBe "docker run --detached fd484f19954f"
      print1[docker :: run :: d :: Name :: `.`](Name("test-container"))         shouldBe "docker run -d test-container"
      print1[docker :: run :: d :: Id :: `.`](Id("fd484f19954f"))               shouldBe "docker run -d fd484f19954f"
    }
  }
}
