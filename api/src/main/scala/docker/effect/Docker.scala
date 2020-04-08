package docker.effect

import _root_.docker.effect.Docker.runPartialTypeApplication
import _root_.docker.effect.algebra._
import _root_.docker.effect.interop.{ Accessor, Command, Provider }
import _root_.docker.effect.syntax.provider._
import shapeless.ops.hlist.Last
import shapeless.{ ::, HList }

abstract class Docker[F[-_, +_]: Provider: Accessor](implicit command: Command[F]) {
  val runContainerN: F[Name, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: run :: Name :: `.`](_)
    }

  val runContainerId: F[Id, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: run :: Id :: `.`](_)
    }

  val runDetachedContainerN: F[Name, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: run :: detach :: Name :: `.`](_)
    }

  val runDetachedContainerId: F[Id, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: run :: detach :: Id :: `.`](_)
    }

  val stopContainerN: F[Name, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: stop :: Name :: `.`](_)
    }

  val stopContainerId: F[Id, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: stop :: Id :: `.`](_)
    }

  val killContainerN: F[Name, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: kill :: Name :: `.`](_)
    }

  val killContainerId: F[Id, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: kill :: Id :: `.`](_)
    }

  val removeContainerN: F[Name, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: rm :: Name :: `.`](_)
    }

  val removeContainerId: F[Id, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: rm :: Id :: `.`](_)
    }

  val forceRemoveContainerN: F[Name, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: rm :: force :: Name :: `.`](_)
    }

  val forceRemoveContainer: F[Id, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: rm :: force :: Id :: `.`](_)
    }

  val pullImage: F[(Name, Tag), SuccessMessage] =
    Accessor.accessM {
      run1[docker :: pull :: (Name, Tag) :: `.`](_)
    }

  val listAllImages: F[Any, SuccessMessage] =
    Accessor.accessM(_ => run0[docker :: images :: all :: `.`])

  val listAllImageIds: F[Any, SuccessMessage] =
    Accessor.accessM(_ => run0[docker :: images :: aq :: `.`])

  val removeImageN: F[Name, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: rmi :: Name :: `.`](_)
    }

  val removeImageId: F[Id, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: rmi :: Id :: `.`](_)
    }

  private[effect] def run0[Cmd <: HList: Valid: Printed]: F[Any, SuccessMessage] =
    command.executed provided printed0[Cmd]

  private[effect] def run1[Cmd <: HList]: runPartialTypeApplication[Cmd, F] =
    new runPartialTypeApplication[Cmd, F]
}

object Docker {
  def apply[F[-_, +_]: Command: Accessor: Provider]: Docker[F] = new Docker[F] {}

  final private[Docker] class runPartialTypeApplication[Cmd <: HList, F[-_, +_]](
    private val d: Boolean = true
  ) extends AnyVal {
    def apply[Tgt, Exp](t: Tgt)(
      implicit
      ev1: Valid[Cmd],
      ev2: Last.Aux[Cmd, Exp],
      ev3: Tgt =:= Exp,
      ev4: Provider[F],
      ev5: Printed[Cmd],
      command: Command[F]
    ): F[Any, SuccessMessage] =
      command.executed provided printed1[Cmd](t)
  }
}
