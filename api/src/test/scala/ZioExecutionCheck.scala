import cats.syntax.either._
import docker.effect.Docker
import docker.effect.algebra.Name
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import zio.ZIO
import syntax.ZioTestSyntax

final class ZioExecutionCheck extends AnyWordSpecLike with Matchers with ZioTestSyntax {
  val docker = Docker[ZIO]

  "a zio docker effect" should {
    "get the list of images" in {
      docker.listAllImages satisfies { res =>
        res.unMk.value shouldBe "a"
      }
    }

    "run a container by name" in {
      (docker.runContainer provide Name("alpine").asLeft) satisfies { res =>
        res.unMk.value shouldBe "a"
      }
    }
  }
}
