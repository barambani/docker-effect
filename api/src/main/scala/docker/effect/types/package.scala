package docker.effect

import docker.effect.internal._
import eu.timepit.refined.W
import eu.timepit.refined.api.{ Refined, RefinedTypeOps }
import eu.timepit.refined.string.{ MatchesRegex, Url }
import eu.timepit.refined.types.numeric.PosInt
import io.circe.Decoder

import scala.concurrent.duration.FiniteDuration

package object types {

  final type |[A, B] = Either[A, B]

  final type EngineHost = String Refined Url
  final object EngineHost extends RefinedTypeOps[EngineHost, String]

  final type EnginePort = PosInt
  final val EnginePort = PosInt

  final val ErrorMessage = MkErrorMessage
  final type ErrorMessage = ErrorMessage.T

  final val WarningMessage = MkWarningMessage
  final type WarningMessage = WarningMessage.T

  object Container {

    final val Id = MkContainerId
    final type Id = Id.T

    final type Name = String Refined MatchesRegex[W.`"/?[a-zA-Z0-9_-]+"`.T]
    final object Name extends RefinedTypeOps[Name, String]

    final case class Create(image: Image.Name)

    final case class Created(id: Container.Id, warnings: Option[List[WarningMessage]])
    object Created {

      implicit val createdDecoder: Decoder[Container.Created] =
        Decoder.instance { c =>
          for {
            id <- c.downField("Id").as[Container.Id]
            ws <- c.downField("Warnings").as[Option[List[WarningMessage]]]
          } yield Container.Created(id, ws)
        }
    }

    final val WaitBeforeKill = newtype[FiniteDuration]
    final type WaitBeforeKill = WaitBeforeKill.T
  }

  object Image {

    final val Id = MkImageId
    final type Id = Id.T

    final val Name = MkImageName
    final type Name = Name.T

    final val Tag = MkImageTag
    final type Tag = Tag.T
  }
}
