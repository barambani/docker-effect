import docker.effect.Docker
import org.scalatest.WordSpecLike
import scalaz.zio.ZIO

final class ZioRunnerSpec extends WordSpecLike with ZioTestFunctions {

  "a zio docker effect" should {

    "get the list of images correctly" in {
      successAssert(Docker[ZIO].listAllImages)(
        res => res.unMk.value shouldBe ""
      )
    }
  }
}
