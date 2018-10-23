package docker
package effect
package types

import docker.effect.internal.{ newtype, MkContainerId }
import eu.timepit.refined.W
import eu.timepit.refined.api.{ Refined, RefinedTypeOps }
import eu.timepit.refined.string.MatchesRegex
import io.circe.Decoder

import scala.concurrent.duration.FiniteDuration

object Container {

  final val Id = MkContainerId
  final type Id = Id.T

  final type Name = String Refined MatchesRegex[W.`"/?[a-zA-Z0-9_-]+"`.T]
  final object Name extends RefinedTypeOps[Name, String]

  final case class Create(image: Image.Name)

  final case class Created(id: Container.Id, warnings: Option[List[WarningMessage]])

  object Created {

    implicit val containerCreatedDecoder: Decoder[Container.Created] =
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
