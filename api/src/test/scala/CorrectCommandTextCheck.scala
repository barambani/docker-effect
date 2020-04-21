import _root_.docker.effect.`.`
import cats.syntax.show._
import docker.effect.algebra.{ Image, _ }
import eu.timepit.refined.auto._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import shapeless.::

final class CorrectCommandTextCheck extends AnyWordSpecLike with Matchers {
  "printing command" should {
    "produce the expected text" in {
      printed0[docker :: images :: `.`].show                             shouldBe "docker images"
      printed0[docker :: images :: all :: `.`].show                      shouldBe "docker images --all"
      printed0[docker :: images :: a :: `.`].show                        shouldBe "docker images -a"
      printed0[docker :: images :: q :: `.`].show                        shouldBe "docker images -q"
      printed0[docker :: images :: aq :: `.`].show                       shouldBe "docker images -aq"
      printed0[docker :: images :: (all, quiet, `no-trunc`) :: `.`].show shouldBe "docker images --all --quiet --no-trunc"

      printed0[docker :: ps :: `.`].show                             shouldBe "docker ps"
      printed0[docker :: ps :: all :: `.`].show                      shouldBe "docker ps --all"
      printed0[docker :: ps :: q :: `.`].show                        shouldBe "docker ps -q"
      printed0[docker :: ps :: (all, quiet, `no-trunc`) :: `.`].show shouldBe "docker ps --all --quiet --no-trunc"

      printed0[docker :: kill :: s :: HUP :: `.`].show                                 shouldBe "docker kill -s=HUP"
      printed0[docker :: kill :: signal :: KILL :: `.`].show                           shouldBe "docker kill --signal=KILL"
      printed1[docker :: kill :: signal :: KILL :: Id :: `.`](Id("fd484f19954f")).show shouldBe "docker kill --signal=KILL fd484f19954f"
      printed1[docker :: kill :: s :: HUP :: Id :: `.`](Id("fd484f19954f")).show       shouldBe "docker kill -s=HUP fd484f19954f"
      printed1[docker :: kill :: Name :: `.`](Name("test-container")).show             shouldBe "docker kill test-container"
      printed1[docker :: kill :: Id :: `.`](Id("fd484f19954f")).show                   shouldBe "docker kill fd484f19954f"

      printed1[docker :: rm :: Id :: `.`](Id("fd484f19954f")).show                shouldBe "docker rm fd484f19954f"
      printed1[docker :: rm :: Name :: `.`](Name("test-container")).show          shouldBe "docker rm test-container"
      printed1[docker :: rm :: force :: Id :: `.`](Id("fd484f19954f")).show       shouldBe "docker rm --force fd484f19954f"
      printed1[docker :: rm :: force :: Name :: `.`](Name("test-container")).show shouldBe "docker rm --force test-container"
      printed1[docker :: rm :: f :: Id :: `.`](Id("fd484f19954f")).show           shouldBe "docker rm -f fd484f19954f"
      printed1[docker :: rm :: f :: Name :: `.`](Name("test-container")).show     shouldBe "docker rm -f test-container"

      printed1[docker :: rmi :: Id :: `.`](Id("fd484f19954f")).show  shouldBe "docker rmi fd484f19954f"
      printed1[docker :: rmi :: Repo :: `.`](Repo("test-repo")).show shouldBe "docker rmi test-repo"
      printed1[docker :: rmi :: (Repo, Tag) :: `.`](Repo("test-repo") -> Tag("a-tag")).show shouldBe "docker rmi test-repo:a-tag"

      printed1[docker :: stop :: Name :: `.`](Name("test-container")).show shouldBe "docker stop test-container"
      printed1[docker :: stop :: Id :: `.`](Id("fd484f19954f")).show       shouldBe "docker stop fd484f19954f"

      printed1[docker :: run :: Image :: `.`](Image("test-image")).show           shouldBe "docker run test-image"
      printed1[docker :: run :: Id :: `.`](Id("fd484f19954f")).show               shouldBe "docker run fd484f19954f"
      printed1[docker :: run :: detach :: Image :: `.`](Image("test-image")).show shouldBe "docker run --detach test-image"
      printed1[docker :: run :: detach :: Id :: `.`](Id("fd484f19954f")).show     shouldBe "docker run --detach fd484f19954f"
      printed1[docker :: run :: d :: Image :: `.`](Image("test-image")).show      shouldBe "docker run -d test-image"
      printed1[docker :: run :: d :: Id :: `.`](Id("fd484f19954f")).show          shouldBe "docker run -d fd484f19954f"

      printed1[docker :: pull :: (Image, Tag) :: `.`](Image("test-image")          -> Tag("a-tag")).show shouldBe "docker pull test-image:a-tag"
      printed1[docker :: run :: detach :: (Image, Tag) :: `.`](Image("test-image") -> Tag("a-tag")).show shouldBe "docker run --detach test-image:a-tag"
    }
  }
}
