import docker.effect.Docker
import org.scalatest.WordSpecLike
import scalaz.zio

final class ZioRunnerSpec extends WordSpecLike with ZioTestFunctions {

  "a zio docker" should {
    "get the list of images correctly" in {

      val io = Docker[zio.IO].listAllImages(())

      successAssert(io)(
        res => res.unMk.value shouldBe ""
      )
    }
  }
}
