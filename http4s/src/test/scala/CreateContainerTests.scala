//import cats.data.EitherT
//import cats.effect.IO
//import cats.syntax.apply._
//import cats.syntax.either._
//import com.github.ghik.silencer.silent
//import docker.effect.http4s.Http4sDocker
//import docker.effect.http4s.syntax._
//import docker.effect.types.{ ErrorMessage, Image }
//import eu.timepit.refined.auto._
//import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }
//
//@silent
//final class CreateContainerTests extends WordSpecLike with Matchers with BeforeAndAfterAll {
//
//  val client = Http4sDocker[IO]("http://localhost", 1717)
//
//  override def beforeAll(): Unit =
//    (client.setupUnixSocketRelay *> IO.unit).unsafeRunSync()
//
//  override def afterAll(): Unit =
//    (client.cleanupUnixSocketRelay *> IO.unit).unsafeRunSync()
//
//  "docker engine api" should {
//
//    "pull an image" in {
//
//      val pulled: Http4sDocker[IO] => EitherT[IO, ErrorMessage, String] =
//        _.pullImage(Image.Repo("library"), Image.Name("alpine"), Image.Tag("latest"))
//
//      val removed: Http4sDocker[IO] => EitherT[IO, ErrorMessage, Unit] =
//        _.removeImage(Image.Name("alpine").asRight)
//
//      val test = for {
//        client <- client
//        pulled <- pulled(client).value
//        _      = pulled.map(s => print(s)).leftMap(failed)
//        //_      <- removed(client).value
//      } yield ()
//
//      test.unsafeRunSync()
//    }
//
////    "pull an image and create a container with it" in {
////
////      val created: Http4sDocker[IO] => EitherT[IO, ErrorMessage, Container.Created] =
////        docker =>
////          docker.pullImage(Image.Repo("library"), Image.Name("alpine"), Image.Tag("latest")) *>
////            docker.createContainer("test-container", Image.Name("alpine"))
////
////      val removed: Http4sDocker[IO] => EitherT[IO, ErrorMessage, Unit] =
////        docker =>
////          docker.removeContainer(Container.Name("test-container").asRight) *>
////            docker.removeImage(Image.Name("alpine").asRight)
////
////      val test = for {
////        client  <- client
////        created <- created(client).value
////        _       = created.map(_.warnings shouldBe empty).leftMap(failed)
////       // _       <- removed(client).value
////      } yield ()
////
////      test.unsafeRunSync()
////    }
//  }
//
//  private[this] def failed[E]: E => Nothing =
//    e => fail(s"Expected success but failed with ErrorMessage: $e")
//}
