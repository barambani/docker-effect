package docker.effect
package interop

import cats.effect.IO
import zio.{ RIO, Task }

sealed trait Provider[F[-_, _], G[_]] {
  def provided[R, A](fa: F[R, A])(r: =>R): G[A]
}

object Provider {
  implicit val zioRioProvider: Provider[RIO, Task] =
    new Provider[RIO, Task] {
      def provided[R, A](fa: RIO[R, A])(r: =>R): Task[A] = fa.provide(r)
    }

  implicit val catsRioProvider: Provider[CatsRIO, IO] =
    new Provider[CatsRIO, IO] {
      def provided[R, A](fa: CatsRIO[R, A])(r: =>R): IO[A] = fa run r
    }
}
