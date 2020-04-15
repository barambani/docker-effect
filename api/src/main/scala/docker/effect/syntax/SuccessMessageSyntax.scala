package docker.effect
package syntax

import cats.syntax.show._
import docker.effect.algebra.{ Id, SuccessMessage }
import docker.effect.syntax.SuccessMessageSyntax.SuccessMessageOps

import scala.language.implicitConversions

private[syntax] trait SuccessMessageSyntax {
  implicit def nonEmptyStringSyntax(sm: SuccessMessage): SuccessMessageOps =
    new SuccessMessageOps(sm)
}

private[syntax] object SuccessMessageSyntax {
  final class SuccessMessageOps(private val sm: SuccessMessage) extends AnyVal {
    def unsafeId: Id = Id.unsafeFrom(sm.show)
  }
}
