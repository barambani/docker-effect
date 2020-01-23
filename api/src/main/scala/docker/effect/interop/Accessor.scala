package docker
package effect
package interop

import zio.ZIO

sealed trait Accessor[F[-_, +_, +_]] {
  def accessM[R, E, A](f: R => F[R, E, A]): F[R, E, A]
}

object Accessor {
  def accessM[R, E, A, F[-_, +_, +_]](f: R => F[R, E, A])(implicit ev: Accessor[F]): F[R, E, A] =
    ev.accessM(f)

  implicit val zioAccessor: Accessor[ZIO] =
    new Accessor[ZIO] {
      def accessM[R, E, A](f: R => ZIO[R, E, A]): ZIO[R, E, A] = ZIO.accessM(f)
    }
}
