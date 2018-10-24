package docker
package effect

import _root_.docker.effect.algebra.evidences.{ Printed, Valid }
import docker.effect.Docker.runPartialTypeApplication
import docker.effect.algebra.algebra._
import shapeless.HList
import shapeless.ops.hlist.Last

trait Docker[F[_, _]] {

  def runContainer: Name | Id => F[ErrorMessage, SuccessMessage]
  def stopContainer: Name | Id => F[ErrorMessage, SuccessMessage]
  def killContainer: Name | Id => F[ErrorMessage, SuccessMessage]
  def removeContainer: Name | Id => F[ErrorMessage, SuccessMessage]
  def forceRemoveContainer: Name | Id => F[ErrorMessage, SuccessMessage]
  def removeAllContainers: Unit => F[ErrorMessage, Unit]

  def pullImage: (Name, Tag) => F[ErrorMessage, Unit]
  def listImages: Unit => F[ErrorMessage, SuccessMessage]
  def removeImage: Name | Id => F[ErrorMessage, Unit]

  def run0[Cmd <: HList](implicit ev1: Valid[Cmd], ev2: Printed[Cmd]): F[ErrorMessage, SuccessMessage] = ???

  def run1[Cmd <: HList]: runPartialTypeApplication[Cmd, F] =
    new runPartialTypeApplication[Cmd, F](true)
}

object Docker {

  final private[Docker] class runPartialTypeApplication[Cmd <: HList, F[_, _]](private val d: Boolean = true)
      extends AnyVal {
    def apply[Tgt, Exp](t: Tgt)(
      implicit
      ev1: Valid[Cmd],
      ev2: Printed[Cmd],
      ev4: Last.Aux[Cmd, Exp],
      ev5: Tgt =:= Exp
    ): F[ErrorMessage, SuccessMessage] = ???
  }

}
