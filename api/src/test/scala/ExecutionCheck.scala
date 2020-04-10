import cats.Functor
import cats.syntax.functor._
import cats.syntax.show._
import docker.effect.algebra.Name
import docker.effect.interop.{ Provider, RioChain }
import docker.effect.syntax.rioChain._
import docker.effect.syntax.provider._
import docker.effect.syntax.successMessage._
import docker.effect.{ CatsRIO, Docker }
import instances.TestRun
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import syntax.TestSyntax
import zio.RIO
import zio.interop.catz

sealed abstract class ExecutionCheck[F[-_, +_]: Provider: RioChain: TestRun](
  docker: Docker[F],
  name: String
) extends AnyWordSpecLike
    with Matchers
    with TestSyntax {

  import docker._

  implicit def F[R]: Functor[F[R, *]]

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
      (runDetachedContainer.map(_.unsafeId) >>> stopContainerId)
        .provided(Name("redis"))
        .satisfies(_.unsafeId.value should not be empty)
    }

    "start a redis instance mapping the port" in {
      (runDetachedContainer.map(_.unsafeId) >>> stopContainerId)
        .provided(Name("redis"))
        .satisfies(_.unsafeId.value should not be empty)
    }
  }
}

final class ZioExecutionCheck extends ExecutionCheck(Docker[RIO], "Zio") {
  def F[R]: Functor[RIO[R, *]] = catz.monadErrorInstance
}
final class CatsExecutionCheck extends ExecutionCheck(Docker[CatsRIO], "Cats IO") {
  def F[R]: Functor[CatsRIO[R, *]] = Functor[CatsRIO[R, *]]
}
