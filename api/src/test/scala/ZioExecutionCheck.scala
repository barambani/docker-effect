import cats.syntax.show._
import docker.effect.Docker
import docker.effect.algebra.Name
import docker.effect.syntax.successMessage._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import eu.timepit.refined.auto._
import syntax.ZioTestSyntax
import zio.RIO

final class ZioExecutionCheck extends AnyWordSpecLike with Matchers with ZioTestSyntax {
  val docker = Docker[RIO]

  "a zio docker effect" should {
    "get the list of images" in {
      docker.listAllImages satisfies { res =>
        val resText = res.show
        resText should startWith("REPOSITORY")
        resText should include("TAG")
        resText should include("IMAGE ID")
        resText should include("CREATED")
        resText should include("SIZE")
      }
    }

    "start a redis instance" in {
      docker.runDetachedContainer.map(_.unsafeId) >>>
        docker.stopContainerId provide Name("redis") satisfies { stopRes =>
        stopRes.unsafeId.value should not be empty
      }
    }

    "start a redis instance mapping the port" in {
      docker.runDetachedContainer.map(_.unsafeId) >>>
        docker.stopContainerId provide Name("redis") satisfies { stopRes =>
        stopRes.unsafeId.value should not be empty
      }
    }
  }
}
