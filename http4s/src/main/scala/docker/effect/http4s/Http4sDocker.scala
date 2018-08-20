package docker.effect
package http4s

import cats.data.EitherT
import com.github.ghik.silencer.silent
import docker.effect.types.Container.WaitBeforeKill
import docker.effect.types.{ |, Container, ErrorMessage, Image }
import org.http4s.client.Client

sealed abstract class Http4sDocker[F[_]: Client] extends Docker[λ[(A, B) => EitherT[F, A, B]]] {

  @silent final private type G[A, B] = EitherT[F, A, B]

  def createContainer: (Container.Name, Image.Name) => G[ErrorMessage, Container.Created] = ???

  def startContainer: Container.Id | Container.Name => G[ErrorMessage, Unit] = ???

  def stopContainer: Container.Id | Container.Name => G[ErrorMessage, Unit] = ???
  def stopContainerSchedule
    : (Container.Id | Container.Name, WaitBeforeKill) => G[ErrorMessage, Unit] = ???

  def waitContainerStop: Container.Id | Container.Name => G[ErrorMessage, Unit] = ???

  def restartContainer: Container.Id | Container.Name => G[ErrorMessage, Unit] = ???
  def restartContainerSchedule
    : (Container.Id | Container.Name, WaitBeforeKill) => G[ErrorMessage, Unit] = ???

  def killContainer: Container.Id | Container.Name => G[ErrorMessage, Unit] = ???

  def removeContainer: Container.Id | Container.Name => G[ErrorMessage, Unit]           = ???
  def forceRemoveContainer: Container.Id | Container.Name => G[ErrorMessage, Unit]      = ???
  def removeContainerAndVolumes: Container.Id | Container.Name => G[ErrorMessage, Unit] = ???

  def removeImage: Image.Id | Image.Name => G[ErrorMessage, Unit] = ???
}

object Http4sDocker {

  def apply[F[_]: Client](implicit I: Http4sDocker[F]): Http4sDocker[F] = I

//  @inline final def apply[F[_]]: LogWriterConstructor1Partially[F] =
//    new LogWriterConstructor1Partially()
//
//  private[effect] type AUX[T, F[_], LWT] =
//    LogWriterConstructor1[T, F] { type LogWriterType = LWT }
//
//  final private[http4s] class Http4sDockerConstructorPartial[F[_]](private val d: Boolean = true) extends AnyVal {
//    @inline def apply(implicit I: Http4sDocker[F]): Http4sDocker[F] = I
//  }
//  final private[effect] class LogWriterConstructor1Partially[F[_]](private val d: Boolean = true)
//      extends AnyVal {
//
//    @inline @silent def apply[T](t: T)(
//      implicit F: Sync[F],
//      LWC: LogWriterConstructor1[T, F]
//    ): F[LWC.LogWriterType] => F[LogWriter[F]] =
//      LWC.evaluation
//  }
//  @inline def apply1[F[_]](client: Client[F]): Http4sDocker[F] = {
//    import Http4sDocker.ioHttp4sDocker
//    implicit val cl: Client[F] = client
//    Http4sDocker[F]
//    //new Http4sDockerConstructorPartial[F]().apply
//  }

//  final private[http4s] class Http4sDockerConstructorPartial[F[_]](private val d: Boolean = true) extends AnyVal {
//    @inline def apply: Http4sDocker[F] = Http4sDocker.apply[F]
//  }

//  implicit def ioHttp4sDocker(implicit cl: Client[IO]): Http4sDocker[IO] =
//    new Http4sDocker[IO] { val client = cl }
}

//trait test[F[_]] {
//
//  def c: Client[IO] = ???
//
//  val const = Http4sDocker.apply1(c)
//  val v: Http4sDocker[IO] = const
//}
