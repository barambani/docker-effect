package docker
package effect

import _root_.docker.effect.algebra.proofs.Printed
import cats.data.Validated.Valid
import com.github.ghik.silencer.silent
import docker.effect.Docker.runPartialTypeApplication
import docker.effect.algebra.algebra._
import shapeless.HList
import shapeless.ops.hlist.Last

@silent trait Docker[F[_, _]] {

  def runContainer: Name | Id => F[ErrorMessage, SuccessMessage]
  def stopContainer: Name | Id => F[ErrorMessage, SuccessMessage]
  def killContainer: Name | Id => F[ErrorMessage, SuccessMessage]
  def removeContainer: Name | Id => F[ErrorMessage, SuccessMessage]
  def forceRemoveContainer: Name | Id => F[ErrorMessage, SuccessMessage]
  def removeAllContainers: Unit => F[ErrorMessage, SuccessMessage]

  def pullImage: (Name, Tag) => F[ErrorMessage, SuccessMessage]
  def listImages: Unit => F[ErrorMessage, SuccessMessage]
  def removeImage: Name | Id => F[ErrorMessage, SuccessMessage]
  def removeAllImages: Unit => F[ErrorMessage, SuccessMessage]

  private[this] def run0[Cmd <: HList](
    implicit
    `_`: Valid[Cmd],
    p: Printed[Cmd]
  ): F[ErrorMessage, SuccessMessage] = ???

  private[this] def run1[Cmd <: HList]: runPartialTypeApplication[Cmd, F] =
    new runPartialTypeApplication[Cmd, F]
}

object Docker {

  final private[Docker] class runPartialTypeApplication[Cmd <: HList, F[_, _]](private val d: Boolean = true)
      extends AnyVal {
    def apply[Tgt, Exp](t: Tgt)(
      implicit
      ev1: Valid[Cmd],
      ev2: Last.Aux[Cmd, Exp],
      ev3: Tgt =:= Exp,
      p: Printed[Cmd]
    ): F[ErrorMessage, SuccessMessage] = ???
  }

}
