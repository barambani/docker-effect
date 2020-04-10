package docker.effect

import _root_.docker.effect.Docker.runPartialTypeApplication
import _root_.docker.effect.algebra._
import _root_.docker.effect.interop.{ Accessor, Command, Provider }
import _root_.docker.effect.syntax.provider._
import cats.effect.IO
import shapeless.ops.hlist.Last
import shapeless.{ ::, HList }
import zio.{ RIO, Task }

abstract class Docker[F[-_, _]: Provider[*[_, _], G]: Accessor[G, *[_, _]], G[_]](
  implicit command: Command[F]
) {
  val runContainer: F[Name, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: run :: Name :: `.`](_)
    }

  val runContainerId: F[Id, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: run :: Id :: `.`](_)
    }

  val runDetachedContainer: F[Name, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: run :: detach :: Name :: `.`](_)
    }

  val runDetachedContainerId: F[Id, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: run :: detach :: Id :: `.`](_)
    }

  val stopContainer: F[Name, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: stop :: Name :: `.`](_)
    }

  val stopContainerId: F[Id, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: stop :: Id :: `.`](_)
    }

  val killContainer: F[Name, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: kill :: Name :: `.`](_)
    }

  val killContainerId: F[Id, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: kill :: Id :: `.`](_)
    }

  val removeContainer: F[Name, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: rm :: Name :: `.`](_)
    }

  val removeContainerId: F[Id, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: rm :: Id :: `.`](_)
    }

  val forceRemoveContainer: F[Name, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: rm :: force :: Name :: `.`](_)
    }

  val forceRemoveContainerId: F[Id, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: rm :: force :: Id :: `.`](_)
    }

  val pullImage: F[(Name, Tag), SuccessMessage] =
    Accessor.accessM {
      run1[docker :: pull :: (Name, Tag) :: `.`](_)
    }

  val listAllImages: F[Unit, SuccessMessage] =
    Accessor.accessM(_ => run0[docker :: images :: all :: `.`])

  val listAllImageIds: F[Unit, SuccessMessage] =
    Accessor.accessM(_ => run0[docker :: images :: aq :: `.`])

  val removeImage: F[Name, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: rmi :: Name :: `.`](_)
    }

  val removeImageId: F[Id, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: rmi :: Id :: `.`](_)
    }

  private[effect] def run0[Cmd <: HList: Valid: Printed]: G[SuccessMessage] =
    command.executed provided printed0[Cmd]

  private[effect] def run1[Cmd <: HList]: runPartialTypeApplication[Cmd, F, G] =
    new runPartialTypeApplication[Cmd, F, G]
}

object Docker {

  @inline final val zio: Docker[RIO, Task]    = Docker[RIO, Task]
  @inline final val cats: Docker[CatsRIO, IO] = Docker[CatsRIO, IO]

  @inline final def apply[F[-_, _]: Command: Provider[*[_, _], G], G[_]: Accessor[*[_], F]]: Docker[F, G] =
    new Docker[F, G] {}

  final private[Docker] class runPartialTypeApplication[Cmd <: HList, F[-_, _], G[_]](
    private val `_`: Boolean = true
  ) extends AnyVal {
    def apply[Tgt, Exp](t: Tgt)(
      implicit
      ev1: Valid[Cmd],
      ev2: Last.Aux[Cmd, Exp],
      ev3: Tgt =:= Exp,
      ev4: Provider[F, G],
      ev5: Printed[Cmd],
      command: Command[F]
    ): G[SuccessMessage] =
      command.executed provided printed1[Cmd](t)
  }
}
