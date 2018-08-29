import cats.data.EitherT
import cats.effect.IO
import docker.effect.http4s.Http4sDocker
import docker.effect.types.Image
import eu.timepit.refined.auto._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

final class CreateContainerTests extends WordSpecLike with Matchers with BeforeAndAfterAll {

  val client = Http4sDocker[IO]("http://localhost", 1717)

  "docker engine api" should {

    "pull an image and create a container with it" in {

      val createdId = for {
        docker  <- EitherT.right(client)
        _       <- docker.pullImage(Image.Name("alpine"), Image.Tag("latest"))
        created <- docker.createContainer("test-container", Image.Name("alpine"))
      } yield created.Id

      createdId.value
        .unsafeRunSync()
        .fold(
          err => fail(s"Expected success but failed with $err"),
          res => res.unMk.value
        )
    }
  }
}
