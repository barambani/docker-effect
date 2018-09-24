import cats.data.EitherT
import cats.effect.IO
import cats.syntax.apply._
import cats.syntax.either._
import docker.effect.http4s.Http4sDocker
import docker.effect.http4s.syntax._
import docker.effect.types.{Container, ErrorMessage, Image}
import eu.timepit.refined.auto._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

final class CreateContainerTests extends WordSpecLike with Matchers with BeforeAndAfterAll {

  val client = Http4sDocker[IO]("http://localhost", 1717)

  override def beforeAll(): Unit =
    (client.setupUnixSocketRelay *> IO.unit).unsafeRunSync()

  override def afterAll(): Unit =
    (client.cleanupUnixSocketRelay *> IO.unit).unsafeRunSync()

  "docker engine api" should {

    "pull an image and create a container with it" in {

      def created(docker: Http4sDocker[IO]): EitherT[IO, ErrorMessage, Container.Created] =
        //docker.pullImage(Image.Name("library/alpine"), Image.Tag("latest")) *>
          docker.createContainer("test-container", Image.Name("library/alpine")) <*
          docker.removeContainer(Container.Name("test-container").asRight)

      client
        .flatMap(cl => created(cl).value)
        .unsafeRunSync()
        .fold(
          err => fail(s"Expected success but failed with ErrorMessage: $err"),
          c => c.warnings shouldBe empty
        )
    }
  }
}
