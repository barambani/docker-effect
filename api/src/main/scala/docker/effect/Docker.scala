package docker
package effect

import _root_.docker.effect.Docker.runPartialTypeApplication
import _root_.docker.effect.algebra._
import _root_.docker.effect.interop.{ Accessor, Provider }
import com.github.ghik.silencer.silent
import _root_.docker.effect.syntax.provider._
import shapeless.ops.hlist.Last
import shapeless.{ ::, HList }

@silent abstract class Docker[F[- _, + _, + _]: Provider: Accessor](implicit command: Command[F]) {

  val runContainer: F[Name | Id, ErrorMessage, SuccessMessage] =
    Accessor.accessM {
      _.fold(
        run1[docker :: run :: Name :: `.`](_),
        run1[docker :: run :: Id :: `.`](_)
      )
    }

  val runDetachedContainer: F[Name | Id, ErrorMessage, SuccessMessage] =
    Accessor.accessM {
      _.fold(
        run1[docker :: run :: detached :: Name :: `.`](_),
        run1[docker :: run :: detached :: Id :: `.`](_)
      )
    }

  val stopContainer: F[Name | Id, ErrorMessage, SuccessMessage] =
    Accessor.accessM {
      _.fold(
        run1[docker :: stop :: Name :: `.`](_),
        run1[docker :: stop :: Id :: `.`](_)
      )
    }

  val killContainer: F[Name | Id, ErrorMessage, SuccessMessage] =
    Accessor.accessM {
      _.fold(
        run1[docker :: kill :: Name :: `.`](_),
        run1[docker :: kill :: Id :: `.`](_)
      )
    }

  val removeContainer: F[Name | Id, ErrorMessage, SuccessMessage] =
    Accessor.accessM {
      _.fold(
        run1[docker :: rm :: Name :: `.`](_),
        run1[docker :: rm :: Id :: `.`](_)
      )
    }

  val forceRemoveContainer: F[Name | Id, ErrorMessage, SuccessMessage] =
    Accessor.accessM {
      _.fold(
        run1[docker :: rm :: force :: Name :: `.`](_),
        run1[docker :: rm :: force :: Id :: `.`](_)
      )
    }

  val pullImage: F[(Name, Tag), ErrorMessage, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: pull :: (Name, Tag) :: `.`](_)
    }

  val listAllImages: F[Any, ErrorMessage, SuccessMessage] =
    Accessor.accessM { _ =>
      run0[docker :: images :: all :: `.`]
    }

  val listAllImageIds: F[Any, ErrorMessage, SuccessMessage] =
    Accessor.accessM { _ =>
      run0[docker :: images :: aq :: `.`]
    }

  val removeImage: F[Name | Id, ErrorMessage, SuccessMessage] =
    Accessor.accessM {
      _.fold(
        run1[docker :: rmi :: Name :: `.`](_),
        run1[docker :: rmi :: Id :: `.`](_)
      )
    }

  private[effect] def run0[Cmd <: HList: Valid: Printed]: F[Any, ErrorMessage, SuccessMessage] =
    command.executed provided print0[Cmd]

  private[effect] def run1[Cmd <: HList]: runPartialTypeApplication[Cmd, F] =
    new runPartialTypeApplication[Cmd, F]
}

object Docker {

  def apply[F[- _, + _, + _]: Command: Accessor: Provider]: Docker[F] = new Docker[F] {}

  final private[Docker] class runPartialTypeApplication[Cmd <: HList, F[- _, + _, + _]](
    private val d: Boolean = true
  ) extends AnyVal {

    def apply[Tgt, Exp](t: Tgt)(
      implicit
      ev1: Valid[Cmd],
      ev2: Last.Aux[Cmd, Exp],
      ev3: Tgt =:= Exp,
      p: Printed[Cmd],
      command: Command[F],
      cccc: Provider[F]
    ): F[Any, ErrorMessage, SuccessMessage] =
      command.executed provided print1[Cmd](t)
  }
}
