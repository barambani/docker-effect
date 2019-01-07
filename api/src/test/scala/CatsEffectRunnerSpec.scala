import docker.effect.{ CatsBio, Docker }
import org.scalatest.WordSpecLike

final class CatsEffectRunnerSpec extends WordSpecLike with CatsBioTestFunctions {

  "a cats effect docker" should {
    "get the list of images correctly" in {

      val io = Docker[CatsBio].listImages(())

      successAssert(io)(
        res => res.unMk.value shouldBe ""
      )
    }
  }
}
