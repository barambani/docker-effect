package docker.effect

import docker.effect.types.Container.WaitBeforeKill
import docker.effect.types.{Container, ErrorMessage, Image, |}

trait Docker[F[_, _]] {

  def createContainer: (Container.Name, Image.Name) => F[ErrorMessage, Container.Created]

  def startContainer: Container.Id | Container.Name => F[ErrorMessage, Unit]

  def stopContainer: Container.Id | Container.Name => F[ErrorMessage, Unit]
  def stopContainerSchedule
    : (Container.Id | Container.Name, WaitBeforeKill) => F[ErrorMessage, Unit]

  def waitContainerStop: Container.Id | Container.Name => F[ErrorMessage, Unit]

  def restartContainer: Container.Id | Container.Name => F[ErrorMessage, Unit]
  def restartContainerSchedule
    : (Container.Id | Container.Name, WaitBeforeKill) => F[ErrorMessage, Unit]

  def killContainer: Container.Id | Container.Name => F[ErrorMessage, Unit]

  def removeContainer: Container.Id | Container.Name => F[ErrorMessage, Unit]
  def forceRemoveContainer: Container.Id | Container.Name => F[ErrorMessage, Unit]
  def removeContainerAndVolumes: Container.Id | Container.Name => F[ErrorMessage, Unit]

  def removeImage: Image.Id | Image.Name => F[ErrorMessage, Unit]
}

object Endpoint {

  import typedapi.dsl._

  val createContainerEp =
    := :> "containers" :> "create" :> Query[Container.Name]("name") :>
      ReqBody[Json, Container.Create] :> Post[Json, Container.Created]

  val startContainerEp =
    := :> "containers" :> "create" :> Query[Container.Name]("name") :>
      ReqBody[Json, Container.Create] :> Post[Json, Container.Created]
}
