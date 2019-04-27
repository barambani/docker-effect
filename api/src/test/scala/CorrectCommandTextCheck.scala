import docker.effect.algebra._
import org.scalatest.{ Matchers, WordSpecLike }
import shapeless.::
import docker.effect.`.`
import cats.syntax.show._

final class CorrectCommandTextCheck extends WordSpecLike with Matchers {

  "printing command" should {
    "produce the expected text" in {
      print0[docker :: images :: `.`].show                                           shouldBe "docker images"
      print0[docker :: images :: all :: `.`].show                                    shouldBe "docker images --all"
      print0[docker :: images :: a :: `.`].show                                      shouldBe "docker images -a"
      print0[docker :: images :: q :: `.`].show                                      shouldBe "docker images -q"
      print0[docker :: images :: aq :: `.`].show                                     shouldBe "docker images -aq"
      print0[docker :: ps :: `.`].show                                               shouldBe "docker ps"
      print0[docker :: ps :: all :: `.`].show                                        shouldBe "docker ps --all"
      print0[docker :: ps :: q :: `.`].show                                          shouldBe "docker ps -q"
      print0[docker :: kill :: signal :: KILL :: `.`].show                           shouldBe "docker kill --signal=KILL"
      print0[docker :: kill :: s :: HUP :: `.`].show                                 shouldBe "docker kill -s=HUP"
      print1[docker :: kill :: Id :: `.`](Id("fd484f19954f")).show                   shouldBe "docker kill fd484f19954f"
      print1[docker :: kill :: signal :: KILL :: Id :: `.`](Id("fd484f19954f")).show shouldBe "docker kill --signal=KILL fd484f19954f"
      print1[docker :: kill :: s :: HUP :: Id :: `.`](Id("fd484f19954f")).show       shouldBe "docker kill -s=HUP fd484f19954f"
      print1[docker :: rmi :: Id :: `.`](Id("fd484f19954f")).show                    shouldBe "docker rmi fd484f19954f"
      print1[docker :: rmi :: Repo :: `.`](Repo("test-repo")).show                   shouldBe "docker rmi test-repo"
      print1[docker :: run :: Name :: `.`](Name("test-container")).show              shouldBe "docker run test-container"
      print1[docker :: run :: Id :: `.`](Id("fd484f19954f")).show                    shouldBe "docker run fd484f19954f"
      print1[docker :: run :: detached :: Name :: `.`](Name("test-container")).show  shouldBe "docker run --detached test-container"
      print1[docker :: run :: detached :: Id :: `.`](Id("fd484f19954f")).show        shouldBe "docker run --detached fd484f19954f"
      print1[docker :: run :: d :: Name :: `.`](Name("test-container")).show         shouldBe "docker run -d test-container"
      print1[docker :: run :: d :: Id :: `.`](Id("fd484f19954f")).show               shouldBe "docker run -d fd484f19954f"
    }
  }
}
