import cats.syntax.show._
import docker.effect.algebra.Name
import docker.effect.syntax.successMessage._
import docker.effect.{ CatsRIO, Docker }
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import syntax.TestSyntax

final class CatsExecutionCheck extends AnyWordSpecLike with Matchers with TestSyntax {
  val docker = Docker[CatsRIO]

  "a cats docker effect" should {
    "get the list of images" in {
      docker.listAllImages.provided(()) satisfies { res =>
        val resText = res.show
        resText should startWith("REPOSITORY")
        resText should include("TAG")
        resText should include("IMAGE ID")
        resText should include("CREATED")
        resText should include("SIZE")
      }
    }

    "start a redis instance" in {
      (docker.runDetachedContainer <&> (_.unsafeId) >>> docker.stopContainerId)
        .provided(Name("redis"))
        .satisfies(_.unsafeId.value should not be empty)
    }

    "start a redis instance mapping the port" in {
      (docker.runDetachedContainer <&> (_.unsafeId) >>> docker.stopContainerId)
        .provided(Name("redis"))
        .satisfies(_.unsafeId.value should not be empty)
    }
  }
}
