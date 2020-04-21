package docker.effect
package syntax

import cats.syntax.show._
import docker.effect.algebra.{ Id, Name, SuccessMessage, TextString }
import docker.effect.syntax.SuccessMessageSyntax.SuccessMessageOps

import scala.language.implicitConversions

private[syntax] trait SuccessMessageSyntax {
  implicit def nonEmptyStringSyntax(sm: SuccessMessage): SuccessMessageOps =
    new SuccessMessageOps(sm)
}

private[syntax] object SuccessMessageSyntax {
  final class SuccessMessageOps(private val sm: SuccessMessage) extends AnyVal {
    def unsafeId: Id               = Id.unsafeFrom(sm.show)
    def safeId: Either[String, Id] = Id.from(sm.show)

    def name: Name = Name(TextString.unsafeFrom(sm.show))
  }
}
