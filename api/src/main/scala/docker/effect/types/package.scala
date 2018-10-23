package docker
package effect

import docker.effect.internal._
import eu.timepit.refined.api.{ Refined, RefinedTypeOps }
import eu.timepit.refined.string.Url
import eu.timepit.refined.types.numeric.PosInt

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
}
