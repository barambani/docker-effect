package docker.effect
package interop

import zio.RIO

sealed trait Accessor[F[-_, +_]] {
  def accessM[R, A](f: R => F[R, A]): F[R, A]
}

object Accessor {
  def accessM[R, A, F[-_, +_]](f: R => F[R, A])(implicit ev: Accessor[F]): F[R, A] =
    ev.accessM(f)

  implicit val zioRioAccessor: Accessor[RIO] =
    new Accessor[RIO] {
      def accessM[R, A](f: R => RIO[R, A]): RIO[R, A] = RIO.accessM(f)
    }

  implicit val catsRioAccessor: Accessor[CatsRIO] =
    new Accessor[CatsRIO] {
      def accessM[R, A](f: R => CatsRIO[R, A]): CatsRIO[R, A] = CatsRIO.accessing(f)
    }
}
