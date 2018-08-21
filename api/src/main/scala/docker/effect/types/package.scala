package docker.effect

import docker.effect.internal.{ newtype, MkErrorMessage, MkWarningMessage }
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.{ MatchesRegex, Url }
import eu.timepit.refined.types.net.PortNumber
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.{ Decoder, Encoder }

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

    final type Id   = NonEmptyString
    final type Name = String Refined MatchesRegex[W.`"/?[a-zA-Z0-9_-]+"`.T]

    final case class Create(image: Image.Name)
    object Create {
      implicit def vaa: Decoder[Container.Create] = ???
      implicit def caa: Encoder[Container.Create] = ???
    }

    final case class Created(id: Id, warnings: List[WarningMessage])
    object Created {
      implicit def vaa1: Decoder[Container.Created] = ???
      implicit def caa1: Encoder[Container.Created] = ???
    }

    final val WaitBeforeKill = newtype[FiniteDuration]
    final type WaitBeforeKill = WaitBeforeKill.T
  }

  object Image {
    final type Id   = NonEmptyString
    final type Name = NonEmptyString
  }
}
