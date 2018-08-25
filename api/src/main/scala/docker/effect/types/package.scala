package docker.effect

import docker.effect.internal._
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.{ MatchesRegex, Url }
import eu.timepit.refined.types.net.PortNumber

import scala.concurrent.duration.FiniteDuration

package object types {

  final type |[A, B] = Either[A, B]

  final type EngineHost = String Refined Url
  final type EnginePort = PortNumber

  final val ErrorMessage = MkErrorMessage
  final type ErrorMessage = ErrorMessage.T

  final val WarningMessage = MkWarningMessage
  final type WarningMessage = WarningMessage.T

  object Container {

    final val Id = MkContainerId
    final type Id = Id.T

    final type Name = String Refined MatchesRegex[W.`"/?[a-zA-Z0-9_-]+"`.T]

    final case class Create(image: Image.Name)
    final case class Created(id: Container.Id, warnings: List[WarningMessage])

    final val WaitBeforeKill = newtype[FiniteDuration]
    final type WaitBeforeKill = WaitBeforeKill.T
  }

  object Image {

    final val Id = MkImageId
    final type Id = Id.T

    final val Name = MkImageName
    final type Name = Name.T
  }
}
