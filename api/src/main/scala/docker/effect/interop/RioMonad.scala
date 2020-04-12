package docker.effect
package interop

import zio.RIO

trait RioMonad[F[-_, _]] extends RioFunctor[F] {
  def pure[R, A](a: A): F[R, A]
  def >>=[R, A, B](fa: F[R, A])(f: A => F[R, B]): F[R, B]
  def >>>[RA, A, RB >: A, B](fa: F[RA, A])(next: F[RB, B]): F[RA, B]

  override def <&>[R, A, B](fa: F[R, A])(f: A => B): F[R, B] =
    >>=(fa)(a => pure(f(a)))
}

object RioMonad {

  @inline final def apply[F[-_, _]](implicit ef: RioMonad[F]): RioMonad[F] = implicitly

  implicit val zioRioMonad: RioMonad[RIO] =
    new RioMonad[RIO] {
      def pure[R, A](a: A): RIO[R, A] = RIO.effectTotal(a)

      def >>=[R, A, B](fa: RIO[R, A])(f: A => RIO[R, B]): RIO[R, B] = fa >>= f

      def >>>[RA, A, RB >: A, B](fa: RIO[RA, A])(next: RIO[RB, B]): RIO[RA, B] = fa >>> next
    }

  implicit val catsRioMonad: RioMonad[CatsRIO] =
    new RioMonad[CatsRIO] {
      def pure[R, A](a: A): CatsRIO[R, A] = CatsRIO.pure(a)

      def >>=[R, A, B](fa: CatsRIO[R, A])(f: A => CatsRIO[R, B]): CatsRIO[R, B] = fa flatMap f

      def >>>[RA, A, RB >: A, B](fa: CatsRIO[RA, A])(next: CatsRIO[RB, B]): CatsRIO[RA, B] =
        fa andThen next
    }
}
