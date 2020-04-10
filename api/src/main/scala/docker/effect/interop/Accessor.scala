package docker.effect
package interop

import cats.effect.IO
import zio.{ RIO, Task }

sealed trait Accessor[G[_], F[-_, _]] {
  def accessM[R, A](f: R => G[A]): F[R, A]
}

object Accessor {
  def accessM[R, A, G[_], F[-_, _]](f: R => G[A])(implicit ev: Accessor[G, F]): F[R, A] =
    ev.accessM(f)

  implicit val zioRioAccessor: Accessor[Task, RIO] =
    new Accessor[Task, RIO] {
      def accessM[R, A](f: R => Task[A]): RIO[R, A] = RIO.accessM(f)
    }

  implicit val catsRioAccessor: Accessor[IO, CatsRIO] =
    new Accessor[IO, CatsRIO] {
      def accessM[R, A](f: R => IO[A]): CatsRIO[R, A] = CatsRIO(f)
    }
}
