import cats.syntax.show._
import docker.effect.Docker
import docker.effect.algebra.Name
import docker.effect.interop.{ Provider, RioChain, RioMonad }
import docker.effect.syntax.provider._
import docker.effect.syntax.rio._
import instances.TestRun
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import syntax.TestSyntax

sealed abstract class ExecutionCheck[F[-_, _]: Provider[*[_, _], G]: RioChain: RioMonad, G[_]: TestRun](
  docker: Docker[F, G],
  name: String
) extends AnyWordSpecLike
    with Matchers
    with TestSyntax {

  import docker._

  s"a $name docker effect" should {
    "get the list of images" in {
      listAllImages.provided(()) satisfies { res =>
        val resText = res.show
        resText should startWith("REPOSITORY")
        resText should include("TAG")
        resText should include("IMAGE ID")
        resText should include("CREATED")
        resText should include("SIZE")
      }
    }

    "start a redis instance" in {
      (runDetachedContainer >>> stopContainerId >>> removeContainerId)
        .provided(Name("redis"))
        .satisfies(_.value should not be empty)
    }

    "start a redis instance mapping the port" in {
      (runDetachedContainer >>>
        stopContainerId.flatTap(_ => RioMonad[F].pure(())) >>>
        removeContainerId)
        .provided(Name("redis"))
        .satisfies(_.value should not be empty)
    }
  }
}

final class ZioExecutionCheck  extends ExecutionCheck(Docker.zio, "Zio")
final class CatsExecutionCheck extends ExecutionCheck(Docker.catsIo, "Cats IO")
