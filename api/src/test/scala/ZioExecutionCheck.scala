import docker.effect.Docker
import docker.effect.algebra.Name
import org.scalatest.{ Matchers, WordSpecLike }
import scalaz.zio.ZIO
import cats.syntax.either._
import syntax.ZioTestSyntax

final class ZioExecutionCheck extends WordSpecLike with Matchers with ZioTestSyntax {
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
