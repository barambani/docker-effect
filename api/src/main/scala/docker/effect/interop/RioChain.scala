package docker.effect
package interop

import zio.RIO

sealed trait RioChain[F[-_, +_]] {
  def >>>[RA, A, RB >: A, B](fa: F[RA, A])(next: F[RB, B]): F[RA, B]
}

object RioChain {

  implicit val zioRioChain: RioChain[RIO] =
    new RioChain[RIO] {
      def >>>[RA, A, RB >: A, B](fa: RIO[RA, A])(next: RIO[RB, B]): RIO[RA, B] = fa >>> next
    }

  implicit val catsRioChain: RioChain[CatsRIO] =
    new RioChain[CatsRIO] {
      def >>>[RA, A, RB >: A, B](fa: CatsRIO[RA, A])(next: CatsRIO[RB, B]): CatsRIO[RA, B] = fa >>> next
    }
}
