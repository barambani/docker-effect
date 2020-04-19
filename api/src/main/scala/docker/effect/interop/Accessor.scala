package docker.effect
package interop

import cats.effect.IO
import zio.{ RIO, Task }

sealed trait Accessor[G[_], F[-_, +_]] {
  def accessM[R, A](f: R => G[A]): F[R, A]
}

object Accessor {
  def liftM: AccessUnitPartiallyApplied     = new AccessUnitPartiallyApplied
  def accessM[R]: AccessPartiallyApplied[R] = new AccessPartiallyApplied[R]

  final private[interop] class AccessPartiallyApplied[R](private val `_`: Boolean = false) extends AnyVal {
    def apply[F[-_, +_], A, G[_]](f: R => G[A])(implicit ev: Accessor[G, F]): F[R, A] = ev.accessM(f)
  }

  final private[interop] class AccessUnitPartiallyApplied(private val `_`: Boolean = false) extends AnyVal {
    def apply[F[-_, +_], A, G[_]](ga: G[A])(implicit ev: Accessor[G, F]): F[Unit, A] =
      ev.accessM(_ => ga)
  }

  implicit val zioRioAccessor: Accessor[Task, RIO] =
    new Accessor[Task, RIO] {
      def accessM[R, A](f: R => Task[A]): RIO[R, A] = RIO.accessM(f)
    }

  implicit val catsRioAccessor: Accessor[IO, CatsRIO] =
    new Accessor[IO, CatsRIO] {
      def accessM[R, A](f: R => IO[A]): CatsRIO[R, A] = CatsRIO(f)
    }
}
