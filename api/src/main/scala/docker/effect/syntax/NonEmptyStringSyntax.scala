package docker.effect
package syntax

import docker.effect.syntax.NonEmptyStringSyntax.NonEmptyStringOps
import eu.timepit.refined.types.string.NonEmptyString

import scala.language.implicitConversions

private[syntax] trait NonEmptyStringSyntax {
  implicit def nonEmptyStringSyntax(nes: NonEmptyString): NonEmptyStringOps =
    new NonEmptyStringOps(nes)
}

private[syntax] object NonEmptyStringSyntax {
  final class NonEmptyStringOps(private val nes: NonEmptyString) {
    def space(other: NonEmptyString): NonEmptyString =
      ++(" ") ++ other

    def +(str: String): NonEmptyString =
      NonEmptyString.unsafeFrom(nes.value ++ str)

    def ++(str: String): andPartialApplication =
      new andPartialApplication(nes.value ++ str)
  }

  final private[syntax] class andPartialApplication(private val s: String) extends AnyVal {
    def ++(other: NonEmptyString): NonEmptyString =
      NonEmptyString.unsafeFrom(s ++ other.value)
  }
}
