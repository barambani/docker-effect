package docker.effect
package interop

import zio.RIO

sealed trait Provider[F[-_, +_]] {
  def provided[R, A](fa: F[R, A])(r: =>R): F[Any, A]
}

object Provider {
  implicit val rioProvider: Provider[RIO] =
    new Provider[RIO] {
      def provided[R, A](fa: RIO[R, A])(r: =>R): RIO[Any, A] = fa.provide(r)
    }
}
