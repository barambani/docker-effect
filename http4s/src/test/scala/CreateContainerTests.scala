import cats.data.EitherT
import cats.effect.IO
import cats.syntax.apply._
import docker.effect.http4s.Http4sDocker
import docker.effect.types.{ Container, ErrorMessage, Image }
import eu.timepit.refined.auto._
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }
import docker.effect.http4s.syntax._

final class CreateContainerTests extends WordSpecLike with Matchers with BeforeAndAfterAll {

  val client = Http4sDocker[IO]("http://localhost", 1717)

  override def beforeAll(): Unit =
    client.setup.unsafeRunSync()

  override def afterAll(): Unit =
    client.cleanup.unsafeRunSync()

  "docker engine api" should {

    "pull an image and create a container with it" in {

      def createdId(docker: Http4sDocker[IO]): EitherT[IO, ErrorMessage, Container.Id] =
        docker.pullImage(Image.Name("alpine"), Image.Tag("latest")) *>
          (docker.createContainer("test-container", Image.Name("alpine")) map (_.Id))

      client
        .flatMap(cl => createdId(cl).value)
        .unsafeRunSync()
        .fold(
          err => fail(s"Expected success but failed with $err"),
          res => res.unMk.value
        )
    }
  }
}
