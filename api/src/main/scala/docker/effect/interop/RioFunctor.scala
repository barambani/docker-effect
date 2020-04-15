package docker.effect
package interop

import zio.RIO

trait RioFunctor[F[-_, _]] {
  def <&>[R, A, B](fa: F[R, A])(f: A => B): F[R, B]
}

object RioFunctor {

  implicit val zioRioFunctor: RioFunctor[RIO] =
    new RioFunctor[RIO] {
      def <&>[R, A, B](fa: RIO[R, A])(f: A => B): RIO[R, B] = fa map f
    }

  implicit val catsRioChain: RioFunctor[CatsRIO] =
    new RioFunctor[CatsRIO] {
      def <&>[R, A, B](fa: CatsRIO[R, A])(f: A => B): CatsRIO[R, B] = fa map f
    }
}
