import _root_.docker.effect.`.`
import cats.syntax.show._
import docker.effect.algebra._
import eu.timepit.refined.auto._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import shapeless.::

final class CorrectCommandTextCheck extends AnyWordSpecLike with Matchers {
  "printing command" should {
    "produce the expected text" in {
      print0[docker :: images :: `.`].show        shouldBe "docker images"
      print0[docker :: images :: all :: `.`].show shouldBe "docker images --all"
      print0[docker :: images :: a :: `.`].show   shouldBe "docker images -a"
      print0[docker :: images :: q :: `.`].show   shouldBe "docker images -q"
      print0[docker :: images :: aq :: `.`].show  shouldBe "docker images -aq"

      print0[docker :: ps :: `.`].show        shouldBe "docker ps"
      print0[docker :: ps :: all :: `.`].show shouldBe "docker ps --all"
      print0[docker :: ps :: q :: `.`].show   shouldBe "docker ps -q"

      print0[docker :: kill :: s :: HUP :: `.`].show                                 shouldBe "docker kill -s=HUP"
      print0[docker :: kill :: signal :: KILL :: `.`].show                           shouldBe "docker kill --signal=KILL"
      print1[docker :: kill :: signal :: KILL :: Id :: `.`](Id("fd484f19954f")).show shouldBe "docker kill --signal=KILL fd484f19954f"
      print1[docker :: kill :: s :: HUP :: Id :: `.`](Id("fd484f19954f")).show       shouldBe "docker kill -s=HUP fd484f19954f"
      print1[docker :: kill :: Name :: `.`](Name("test-container")).show             shouldBe "docker kill test-container"
      print1[docker :: kill :: Id :: `.`](Id("fd484f19954f")).show                   shouldBe "docker kill fd484f19954f"

      print1[docker :: rm :: Id :: `.`](Id("fd484f19954f")).show                shouldBe "docker rm fd484f19954f"
      print1[docker :: rm :: Name :: `.`](Name("test-container")).show          shouldBe "docker rm test-container"
      print1[docker :: rm :: force :: Id :: `.`](Id("fd484f19954f")).show       shouldBe "docker rm --force fd484f19954f"
      print1[docker :: rm :: force :: Name :: `.`](Name("test-container")).show shouldBe "docker rm --force test-container"
      print1[docker :: rm :: f :: Id :: `.`](Id("fd484f19954f")).show           shouldBe "docker rm -f fd484f19954f"
      print1[docker :: rm :: f :: Name :: `.`](Name("test-container")).show     shouldBe "docker rm -f test-container"

      print1[docker :: rmi :: Id :: `.`](Id("fd484f19954f")).show  shouldBe "docker rmi fd484f19954f"
      print1[docker :: rmi :: Repo :: `.`](Repo("test-repo")).show shouldBe "docker rmi test-repo"

      print1[docker :: run :: Name :: `.`](Name("test-container")).show             shouldBe "docker run test-container"
      print1[docker :: run :: Id :: `.`](Id("fd484f19954f")).show                   shouldBe "docker run fd484f19954f"
      print1[docker :: run :: detached :: Name :: `.`](Name("test-container")).show shouldBe "docker run --detached test-container"
      print1[docker :: run :: detached :: Id :: `.`](Id("fd484f19954f")).show       shouldBe "docker run --detached fd484f19954f"
      print1[docker :: run :: d :: Name :: `.`](Name("test-container")).show        shouldBe "docker run -d test-container"
      print1[docker :: run :: d :: Id :: `.`](Id("fd484f19954f")).show              shouldBe "docker run -d fd484f19954f"

      print1[docker :: stop :: Name :: `.`](Name("test-container")).show shouldBe "docker stop test-container"
      print1[docker :: stop :: Id :: `.`](Id("fd484f19954f")).show       shouldBe "docker stop fd484f19954f"

      print1[docker :: pull :: (Name, Tag) :: `.`](Name("test-container") -> Tag("a-tag")).show shouldBe "docker pull test-container:a-tag"
    }
  }
}
