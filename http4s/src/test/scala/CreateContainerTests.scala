import cats.data.EitherT
import cats.effect.IO
import docker.effect.http4s.Http4sDocker
import docker.effect.types.Image
import eu.timepit.refined.auto._
import org.http4s.client.blaze.Http1Client
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

final class CreateContainerTests extends WordSpecLike with Matchers with BeforeAndAfterAll {

  val client = Http1Client[IO]() map (
    cl => Http4sDocker(cl)("file:///var/run/docker.sock", 80)
  )

  "docker engine api CreateContainer" should {

    "create a container of the expected image" in {

      val id = for {
        docker  <- EitherT.right(client)
        created <- docker.createContainer("test-container", Image.Name("alpine"))
      } yield created.id

      id.value
        .unsafeRunSync()
        .fold(
          err => fail(s"Expected success but failed with $err"),
          res => res.unMk.value
        )
    }
  }
}
