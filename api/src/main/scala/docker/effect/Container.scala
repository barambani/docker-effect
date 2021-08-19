package docker.effect

import cats.Functor
import cats.effect.{IO, Resource}
import docker.effect.algebra.{Id, Image, Tag}
import docker.effect.interop.{RioApplication, RioMonadError}
import docker.effect.syntax.provider._
import docker.effect.syntax.rio._
import zio.{RIO, Task}
import zio.interop.catz._

sealed abstract class Container[F[-_, +_], G[_]](
  implicit ev0: RioApplication[F, G],
  ev1: Functor[G],
  rm: RioMonadError[F]
) {
  val docker: Docker[F, G]
  import docker._

  /** Creates a resource running a detached container by name. The container will be stopped and deleted by
    * the finalizer.
    */
  def detached(i: Image): Resource[G, Id] =
    Resource.make(runDetachedContainer appliedAt i)(id =>
      (stopContainerId >>> removeContainerId *> rm.unit) appliedAt id
    )

  /** Creates a resource running a detached container by name and tag. The container will be stopped and
    * deleted by the finalizer.
    */
  def detached(i: Image, t: Tag): Resource[G, Id] =
    Resource.make(runTaggedDetachedContainer appliedAt (i -> t))(id =>
      (stopContainerId >>> removeContainerId *> rm.unit) appliedAt id
    )
}

object Container {
  @inline final val zio: Container[RIO, Task]      = new Container[RIO, Task] { val docker = Docker.zio }
  @inline final val catsIo: Container[CatsRIO, IO] = new Container[CatsRIO, IO] { val docker = Docker.catsIo }
}
