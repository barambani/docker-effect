import _root_.docker.effect.`.`
import cats.instances.string._
import cats.syntax.eq._
import cats.syntax.show._
import docker.effect.algebra._
import eu.timepit.refined.auto._
import shapeless.::

final class CorrectCommandTextCheck extends munit.FunSuite {

  private[this] val aTag       = Tag("a-tag")
  private[this] val anId       = Id("fd484f19954f")
  private[this] val aRepo      = Repo("test-repo")
  private[this] val anImage    = Image("test-image")
  private[this] val aContainer = Name("test-container")

  test("printed docker images") {
    assert(printed0[docker :: images :: `.`].show === "docker images")
    assert(printed0[docker :: images :: all :: `.`].show === "docker images --all")
    assert(printed0[docker :: images :: a :: `.`].show === "docker images -a")
    assert(printed0[docker :: images :: q :: `.`].show === "docker images -q")
    assert(printed0[docker :: images :: aq :: `.`].show === "docker images -aq")
    assert(
      printed0[
        docker :: images :: (all, quiet, `no-trunc`) :: `.`
      ].show === "docker images --all --quiet --no-trunc"
    )
  }

  test("printed docker ps") {
    assert(printed0[docker :: ps :: `.`].show === "docker ps")
    assert(printed0[docker :: ps :: all :: `.`].show === "docker ps --all")
    assert(printed0[docker :: ps :: q :: `.`].show === "docker ps -q")
    assert(
      printed0[docker :: ps :: (all, quiet, `no-trunc`) :: `.`].show === "docker ps --all --quiet --no-trunc"
    )
  }

  test("printed docker kill") {
    assert(printed0[docker :: kill :: s :: HUP :: `.`].show === "docker kill -s=HUP")
    assert(printed0[docker :: kill :: signal :: KILL :: `.`].show === "docker kill --signal=KILL")
    assert(
      printed1[docker :: kill :: signal :: KILL :: Id :: `.`](
        anId
      ).show === "docker kill --signal=KILL fd484f19954f"
    )
    assert(printed1[docker :: kill :: s :: HUP :: Id :: `.`](anId).show === "docker kill -s=HUP fd484f19954f")
    assert(printed1[docker :: kill :: Name :: `.`](aContainer).show === "docker kill test-container")
    assert(printed1[docker :: kill :: Id :: `.`](anId).show === "docker kill fd484f19954f")
  }

  test("printed docker rm") {
    assert(printed1[docker :: rm :: Id :: `.`](anId).show === "docker rm fd484f19954f")
    assert(printed1[docker :: rm :: Name :: `.`](aContainer).show === "docker rm test-container")
    assert(printed1[docker :: rm :: force :: Id :: `.`](anId).show === "docker rm --force fd484f19954f")
    assert(
      printed1[docker :: rm :: force :: Name :: `.`](aContainer).show === "docker rm --force test-container"
    )
    assert(printed1[docker :: rm :: f :: Id :: `.`](anId).show === "docker rm -f fd484f19954f")
    assert(printed1[docker :: rm :: f :: Name :: `.`](aContainer).show === "docker rm -f test-container")
  }

  test("printed docker rmi") {
    printed1[docker :: rmi :: Id :: `.`](anId).show === "docker rmi fd484f19954f"
    printed1[docker :: rmi :: Repo :: `.`](aRepo).show === "docker rmi test-repo"
    printed1[docker :: rmi :: (Repo, Tag) :: `.`](aRepo -> aTag).show === "docker rmi test-repo:a-tag"
  }

  test("printed docker stop") {
    printed1[docker :: stop :: Id :: `.`](anId).show === "docker stop fd484f19954f"
    printed1[docker :: stop :: Name :: `.`](aContainer).show === "docker stop test-container"
  }

  test("printed docker run") {
    //printed1[docker :: run :: Name :: Image :: `.`](Name("a-name"), anImage).show === "docker run test-image"
    printed1[docker :: run :: Id :: `.`](anId).show === "docker run fd484f19954f"
    printed1[docker :: run :: Image :: `.`](anImage).show === "docker run test-image"

    printed1[docker :: run :: detach :: Image :: `.`](anImage).show === "docker run --detach test-image"
    printed1[docker :: run :: detach :: (Image, Tag) :: `.`](
      anImage -> aTag
    ).show === "docker run --detach test-image:a-tag"
    printed1[docker :: run :: detach :: Id :: `.`](anId).show === "docker run --detach fd484f19954f"

    printed1[docker :: run :: d :: Image :: `.`](anImage).show === "docker run -d test-image"
    printed1[docker :: run :: d :: (Image, Tag) :: `.`](
      anImage -> aTag
    ).show === "docker run -d test-image:a-tag"
    printed1[docker :: run :: d :: Id :: `.`](anId).show === "docker run -d fd484f19954f"
  }

  test("printed docker pull") {
    printed1[docker :: pull :: (Image, Tag) :: `.`](anImage -> aTag).show === "docker pull test-image:a-tag"
  }
}
