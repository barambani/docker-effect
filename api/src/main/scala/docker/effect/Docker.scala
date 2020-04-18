package docker.effect

import _root_.docker.effect.Docker.runPartialTypeApplicationTuple
import _root_.docker.effect.algebra._
import _root_.docker.effect.interop.RioMonadError.absolve
import _root_.docker.effect.interop.{ Accessor, Command, RioApplication, RioMonadError }
import _root_.docker.effect.syntax.provider._
import _root_.docker.effect.syntax.rio._
import _root_.docker.effect.syntax.successMessage._
import cats.effect.IO
import shapeless.ops.hlist.Last
import shapeless.{ ::, HList }
import zio.{ RIO, Task }

sealed abstract class Docker[F[-_, _], G[_]](
  implicit
  ev0: RioMonadError[F],
  ev1: Accessor[G, F],
  ev2: RioApplication[F, G],
  command: Command[F]
) {
  val runContainer: F[Name, Id] =
    absolve(
      Accessor.accessM[Name] {
        run1[docker :: run :: Name :: `.`](_)
      } <&> (_.safeId)
    )

  val runContainerId: F[Id, Id] =
    absolve(
      Accessor.accessM[Id] {
        run1[docker :: run :: Id :: `.`](_)
      } <&> (_.safeId)
    )

  val runDetachedContainer: F[Name, Id] =
    absolve(
      Accessor.accessM[Name] {
        run1[docker :: run :: detach :: Name :: `.`](_)
      } <&> (_.safeId)
    )

  val runTaggedDetachedContainer: F[(Name, Tag), Id] =
    absolve(
      Accessor.accessM[(Name, Tag)] {
        run1[docker :: run :: detach :: (Name, Tag) :: `.`](_)
      } <&> (_.safeId)
    )

  val runDetachedContainerId: F[Id, Id] =
    absolve(
      Accessor.accessM[Id] {
        run1[docker :: run :: detach :: Id :: `.`](_)
      } <&> (_.safeId)
    )

  val stopContainer: F[Name, Id] =
    absolve(
      Accessor.accessM[Name] {
        run1[docker :: stop :: Name :: `.`](_)
      } <&> (_.safeId)
    )

  val stopContainerId: F[Id, Id] =
    absolve(
      Accessor.accessM[Id] {
        run1[docker :: stop :: Id :: `.`](_)
      } <&> (_.safeId)
    )

  val killContainer: F[Name, Id] =
    absolve(
      Accessor.accessM[Name] {
        run1[docker :: kill :: Name :: `.`](_)
      } <&> (_.safeId)
    )

  val killContainerId: F[Id, Id] =
    absolve(
      Accessor.accessM[Id] {
        run1[docker :: kill :: Id :: `.`](_)
      } <&> (_.safeId)
    )

  val removeContainer: F[Name, Id] =
    absolve(
      Accessor.accessM[Name] {
        run1[docker :: rm :: Name :: `.`](_)
      } <&> (_.safeId)
    )

  val removeContainerId: F[Id, Id] =
    absolve(
      Accessor.accessM[Id] {
        run1[docker :: rm :: Id :: `.`](_)
      } <&> (_.safeId)
    )

  val forceRemoveContainer: F[Name, Id] =
    absolve(
      Accessor.accessM[Name] {
        run1[docker :: rm :: force :: Name :: `.`](_)
      } <&> (_.safeId)
    )

  val forceRemoveContainerId: F[Id, Id] =
    absolve(
      Accessor.accessM[Id] {
        run1[docker :: rm :: force :: Id :: `.`](_)
      } <&> (_.safeId)
    )

  val pullImage: F[(Name, Tag), SuccessMessage] =
    Accessor.accessM {
      run1[docker :: pull :: (Name, Tag) :: `.`](_)
    }

  val listAllImages: F[Unit, SuccessMessage] =
    Accessor.liftM(run0[docker :: images :: all :: `.`])

  val listAllImageIds: F[Unit, SuccessMessage] =
    Accessor.liftM(run0[docker :: images :: (all, quiet, `no-trunc`) :: `.`])

  val listAllContainerIds: F[Unit, SuccessMessage] =
    Accessor.liftM(run0[docker :: ps :: (all, quiet, `no-trunc`) :: `.`])

  val removeImage: F[Name, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: rmi :: Name :: `.`](_)
    }

  val removeImageId: F[Id, SuccessMessage] =
    Accessor.accessM {
      run1[docker :: rmi :: Id :: `.`](_)
    }

  private[effect] def run0[Cmd <: HList: Valid: Printed]: G[SuccessMessage] =
    command.executed appliedTo printed0[Cmd]

  private[effect] def run1[Cmd <: HList]: runPartialTypeApplicationTuple[Cmd, F, G] =
    new runPartialTypeApplicationTuple[Cmd, F, G]
}

object Docker {

  @inline final val zio: Docker[RIO, Task]      = Docker[RIO, Task]
  @inline final val catsIo: Docker[CatsRIO, IO] = Docker[CatsRIO, IO]

  @inline final def apply[F[-_, _], G[_]](
    implicit
    ev0: RioMonadError[F],
    ev2: RioApplication[F, G],
    ev3: Accessor[G, F],
    ev4: Command[F]
  ): Docker[F, G] = new Docker[F, G] {}

  final private[Docker] class runPartialTypeApplicationTuple[Cmd <: HList, F[-_, _], G[_]](
    private val `_`: Boolean = true
  ) extends AnyVal
      with runPartialTypeApplication[Cmd, F, G] {
    def apply[Tgt, ExpA, ExpB](t: (ExpA, ExpB))(
      implicit
      ev1: Valid[Cmd],
      ev2: Last.Aux[Cmd, Tgt],
      ev3: Tgt =:= (ExpA, ExpB),
      ev4: RioApplication[F, G],
      ev5: Printed[Cmd],
      command: Command[F]
    ): G[SuccessMessage] =
      command.executed appliedTo printed1[Cmd](t)
  }

  sealed private[Docker] trait runPartialTypeApplication[Cmd <: HList, F[-_, _], G[_]] extends Any {
    def apply[Tgt, Exp](t: Tgt)(
      implicit
      ev1: Valid[Cmd],
      ev2: Last.Aux[Cmd, Exp],
      ev3: Tgt =:= Exp,
      ev4: RioApplication[F, G],
      ev5: Printed[Cmd],
      command: Command[F]
    ): G[SuccessMessage] =
      command.executed appliedTo printed1[Cmd](t)
  }
}
