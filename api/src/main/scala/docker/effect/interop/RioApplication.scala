package docker.effect
package interop

import cats.effect.IO
import zio.{RIO, Task}

sealed trait RioApplication[F[-_, +_], G[_]] {
  def applied[R, A](fa: F[R, A])(r: =>R): G[A]
}

object RioApplication {
  implicit val zioRioProvider: RioApplication[RIO, Task] =
    new RioApplication[RIO, Task] {
      def applied[R, A](fa: RIO[R, A])(r: =>R): Task[A] = fa.provide(r)
    }

  implicit val catsRioProvider: RioApplication[CatsRIO, IO] =
    new RioApplication[CatsRIO, IO] {
      def applied[R, A](fa: CatsRIO[R, A])(r: =>R): IO[A] = fa.rio(r)
    }
}
