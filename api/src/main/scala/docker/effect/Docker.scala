package docker.effect

import docker.effect.Docker.runPartialTypeApplication
import docker.effect.algebra._
import docker.effect.interop.{ Accessor, Command, Provider, RioFunctor }
import docker.effect.syntax.provider._
import docker.effect.syntax.rio._
import docker.effect.syntax.successMessage._
import cats.effect.IO
import shapeless.ops.hlist.Last
import shapeless.{ ::, HList }
import zio.{ RIO, Task }

abstract class Docker[F[-_, _], G[_]](
  implicit
  ev0: RioFunctor[F],
  ev1: Accessor[G, F],
  ev2: Provider[F, G],
  command: Command[F]
) {
  val runContainer: F[Name, Id] =
    Accessor.accessM[Name] {
      run1[docker :: run :: Name :: `.`](_)
    } <&> (_.unsafeId)

  val runContainerId: F[Id, Id] =
    Accessor.accessM[Id] {
      run1[docker :: run :: Id :: `.`](_)
    } <&> (_.unsafeId)

  val runDetachedContainer: F[Name, Id] =
    Accessor.accessM[Name] {
      run1[docker :: run :: detach :: Name :: `.`](_)
    } <&> (_.unsafeId)

  val runDetachedContainerId: F[Id, Id] =
    Accessor.accessM[Id] {
      run1[docker :: run :: detach :: Id :: `.`](_)
    } <&> (_.unsafeId)

  val stopContainer: F[Name, Id] =
    Accessor.accessM[Name] {
      run1[docker :: stop :: Name :: `.`](_)
    } <&> (_.unsafeId)

  val stopContainerId: F[Id, Id] =
    Accessor.accessM[Id] {
      run1[docker :: stop :: Id :: `.`](_)
    } <&> (_.unsafeId)

  val killContainer: F[Name, Id] =
    Accessor.accessM[Name] {
      run1[docker :: kill :: Name :: `.`](_)
    } <&> (_.unsafeId)

  val killContainerId: F[Id, Id] =
    Accessor.accessM[Id] {
      run1[docker :: kill :: Id :: `.`](_)
    } <&> (_.unsafeId)

  val removeContainer: F[Name, Id] =
    Accessor.accessM[Name] {
      run1[docker :: rm :: Name :: `.`](_)
    } <&> (_.unsafeId)

  val removeContainerId: F[Id, Id] =
    Accessor.accessM[Id] {
      run1[docker :: rm :: Id :: `.`](_)
    } <&> (_.unsafeId)

  val forceRemoveContainer: F[Name, Id] =
    Accessor.accessM[Name] {
      run1[docker :: rm :: force :: Name :: `.`](_)
    } <&> (_.unsafeId)

  val forceRemoveContainerId: F[Id, Id] =
    Accessor.accessM[Id] {
      run1[docker :: rm :: force :: Id :: `.`](_)
    } <&> (_.unsafeId)

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

  @inline final val zio: Docker[RIO, Task]      = Docker[RIO, Task]
  @inline final val catsIo: Docker[CatsRIO, IO] = Docker[CatsRIO, IO]

  @inline final def apply[F[-_, _], G[_]](
    implicit
    ev0: RioFunctor[F],
    ev2: Provider[F, G],
    ev3: Accessor[G, F],
    ev4: Command[F]
  ): Docker[F, G] =
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
