package docker.effect
package interop

import zio.ZIO

sealed trait Provider[F[-_, +_, +_]] {
  def provided[R, E, A](fa: F[R, E, A])(r: =>R): F[Any, E, A]
}

object Provider {
  implicit val zioProvider: Provider[ZIO] =
    new Provider[ZIO] {
      def provided[R, E, A](fa: ZIO[R, E, A])(r: =>R): ZIO[Any, E, A] = fa.provide(r)
    }
}
