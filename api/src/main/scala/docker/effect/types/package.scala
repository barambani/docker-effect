package docker.effect

import docker.effect.internal.newType
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.types.string.NonEmptyString
import shapeless.{ ::, HNil }

package object types {

  final type |[A, B] = Either[A, B]

  final val ErrorMessage = newType[String]
  final type ErrorMessage = ErrorMessage.T

  final val WarningMessage = newType[String]
  final type WarningMessage = WarningMessage.T

  object Container {
    final type Id      = NonEmptyString
    final type Name    = String Refined MatchesRegex[W.`"/?[a-zA-Z0-9_-]+"`.T]
    final type Image   = NonEmptyString
    final type Created = Id :: List[WarningMessage] :: HNil
  }
}
