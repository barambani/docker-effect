package docker
package effect

import _root_.docker.effect.algebra._
import com.github.ghik.silencer.silent
import _root_.docker.effect.Docker.runPartialTypeApplication
import shapeless.ops.hlist.Last
import shapeless.{ ::, HList, HNil }

@silent abstract class Docker[F[_, _]](implicit exec: Exec[F]) {

  val runContainer: Name | Id => F[ErrorMessage, SuccessMessage] =
    _.fold(
      run1[docker :: run :: Name :: HNil](_),
      run1[docker :: run :: Id :: HNil](_)
    )

  val stopContainer: Name | Id => F[ErrorMessage, SuccessMessage]        = ???
  val killContainer: Name | Id => F[ErrorMessage, SuccessMessage]        = ???
  val removeContainer: Name | Id => F[ErrorMessage, SuccessMessage]      = ???
  val forceRemoveContainer: Name | Id => F[ErrorMessage, SuccessMessage] = ???
  val removeAllContainers: Unit => F[ErrorMessage, SuccessMessage]       = ???

  val pullImage: (Name, Tag) => F[ErrorMessage, SuccessMessage] = ???

  val listAllImages: Unit => F[ErrorMessage, SuccessMessage] =
    _ => run0[docker :: images :: all :: HNil]

  val listAllImageIds: Unit => F[ErrorMessage, SuccessMessage] =
    _ => run0[docker :: images :: aq :: HNil]

  val removeImage: Name | Id => F[ErrorMessage, SuccessMessage] = ???
  val removeAllImages: Unit => F[ErrorMessage, SuccessMessage]  = ???

  private[effect] def run0[Cmd <: HList: Valid: Printed]: F[ErrorMessage, SuccessMessage] =
    exec.run(print0[Cmd])

  private[effect] def run1[Cmd <: HList]: runPartialTypeApplication[Cmd, F] =
    new runPartialTypeApplication[Cmd, F]
}

object Docker {

  def apply[F[_, _]: Exec]: Docker[F] = new Docker[F] {}

  final private[Docker] class runPartialTypeApplication[Cmd <: HList, F[_, _]](
    private val d: Boolean = true
  ) extends AnyVal {

    def apply[Tgt, Exp](t: Tgt)(
      implicit
      ev1: Valid[Cmd],
      ev2: Last.Aux[Cmd, Exp],
      ev3: Tgt =:= Exp,
      p: Printed[Cmd],
      exec: Exec[F]
    ): F[ErrorMessage, SuccessMessage] =
      exec.run(print1[Cmd](t))
  }
}
