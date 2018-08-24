package docker.effect
package http4s

import cats.data.EitherT
import cats.effect.{ Effect, Sync }
import com.github.ghik.silencer.silent
import docker.effect.DockerApiEndpoints._
import docker.effect.types.Container.WaitBeforeKill
import docker.effect.types.{ ErrorMessage, _ }
import org.http4s.Status._
import org.http4s.circe.{ jsonEncoderOf, jsonOf }
import org.http4s.client.Client
import typedapi.client._
import typedapi.client.http4s._
import internal.syntax._

sealed abstract class Http4sDocker[F[_]: Effect](
  client: Client[F],
  host: EngineHost,
  port: EnginePort
) extends MaterializedApi[F]
    with Docker[λ[(A, B) => EitherT[F, A, B]]] {

  @silent final private type G[A, B] = EitherT[F, A, B]

  final private val clientManager =
    ClientManager(client, host.value, port.value)

  implicit private val errorDecoder = jsonOf[F, ErrorMessage]

  def createContainer: (Container.Name, Image.Name) => G[ErrorMessage, Container.Created] =
    (cn, in) => {

      implicit val encoder = jsonEncoderOf[F, Container.Create]
      implicit val decoder = jsonOf[F, Container.Created]

      createContainerC(cn, Container.Create(in))
        .run[F]
        .raw(clientManager)
        .handleRequestWith {
          case Ok(r)         => EitherT.right(r.as[Container.Created])
          case BadRequest(_) => EitherT.leftT(ErrorMessage("400 -> Bad parameter"))
          case Conflict(_)   => EitherT.leftT(ErrorMessage("409 -> Conflict"))
          case other         => EitherT.left(other.as[ErrorMessage])
        }
    }

  def startContainer: Container.Id | Container.Name => G[ErrorMessage, Unit] =
    eitherNameOrId => {

      val response = eitherNameOrId.fold(
        id => startContainerByIdC(id).run[F].raw(clientManager),
        name => startContainerByNameC(name).run[F].raw(clientManager)
      )

      response.handleRequestWith {
        case Ok(_)          => EitherT.rightT(())
        case NotModified(_) => EitherT.leftT(ErrorMessage("304 -> Container already started"))
        case other          => EitherT.left(other.as[ErrorMessage])
      }
    }

  def stopContainer: Container.Id | Container.Name => G[ErrorMessage, Unit] =
    ???
  def stopContainerSchedule: (Container.Id | Container.Name, WaitBeforeKill) => G[ErrorMessage, Unit] =
    ???

  def waitContainerStop: Container.Id | Container.Name => G[ErrorMessage, Unit] = ???

  def restartContainer: Container.Id | Container.Name => G[ErrorMessage, Unit] =
    ???
  def restartContainerSchedule: (Container.Id | Container.Name, WaitBeforeKill) => G[ErrorMessage, Unit] =
    ???

  def killContainer: Container.Id | Container.Name => G[ErrorMessage, Unit] =
    ???

  def removeContainer: Container.Id | Container.Name => G[ErrorMessage, Unit] =
    ???
  def forceRemoveContainer: Container.Id | Container.Name => G[ErrorMessage, Unit] = ???

  def removeContainerAndVolumes: Container.Id | Container.Name => G[ErrorMessage, Unit] = ???

  def removeImage: Image.Id | Image.Name => G[ErrorMessage, Unit] = ???
}

abstract class MaterializedApi[F[_]: Sync] {

  val (createContainerC, startContainerByIdC, startContainerByNameC) =
    deriveAll(
      createContainerEp :|:
        startContainerByIdEp :|:
        startContainerByNameEp
    )
}

object Http4sDocker {}
