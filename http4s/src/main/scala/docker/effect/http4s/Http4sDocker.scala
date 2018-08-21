package docker.effect
package http4s

import cats.data.EitherT
import cats.effect.{Effect, Sync}
import com.github.ghik.silencer.silent
import docker.effect.Endpoint._
import docker.effect.types.Container.WaitBeforeKill
import docker.effect.types._
import org.http4s.circe.{jsonOf, jsonEncoderOf}
import org.http4s.client.Client
import typedapi.client._
import typedapi.client.http4s._
import internal.syntax._

sealed abstract class Http4sDocker[F[_]: Effect](client: Client[F], host: EngineHost, port: EnginePort)
  extends MaterializedApi[F]
  with Docker[λ[(A, B) => EitherT[F, A, B]]] {

  @silent final private type G[A, B] = EitherT[F, A, B]

  private final val clientManager = ClientManager(client, host.value, port.value)

  private implicit val errorDecoder = jsonOf[F, ErrorMessage]

  def createContainer: (Container.Name, Image.Name) => G[ErrorMessage, Container.Created] =
    (cn, in) => {

      implicit val encoder = jsonEncoderOf[F, Container.Create]
      implicit val decoder = jsonOf[F, Container.Created]

      createContainerApi(cn, Container.Create(in))
        .run[F]
        .raw(clientManager)
        .handleFor[Container.Created]
    }

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

abstract class MaterializedApi[F[_]: Sync] {

//  implicit val decoder = jsonOf[F, Container.Create]
//  implicit val encoder = jsonEncoderOf[F, Container.Create]
//
//  implicit val decoder1 = jsonOf[F, Container.Created]
//  implicit val encoder1 = jsonEncoderOf[F, Container.Created]

  val (createContainerApi, startContainerApi) =
    deriveAll(
      createContainerEp :|:
      startContainerEp
    )
}

object Http4sDocker {
  
}
