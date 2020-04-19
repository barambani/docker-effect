package docker.effect
package interop

import cats.MonadError
import zio.RIO

trait RioFunctor[F[-_, +_]] {
  def <&>[R, A, B](fa: F[R, A])(f: A => B): F[R, B]
}

object RioFunctor {
  @inline final def apply[F[-_, +_]](implicit ef: RioFunctor[F]): RioFunctor[F] = implicitly

  implicit val zioRioMonadError: RioMonadError[RIO] =
    new RioMonadError[RIO] {
      def pure[R, A](a: A): RIO[R, A] = RIO.effectTotal(a)

      def >>=[R, A, B](fa: RIO[R, A])(f: A => RIO[R, B]): RIO[R, B] = fa >>= f

      def >>>[RA, A, RB >: A, B](fa: RIO[RA, A])(next: RIO[RB, B]): RIO[RA, B] = fa >>> next

      def <&>[R, A, B](fa: RIO[R, A])(f: A => B): RIO[R, B] = fa map f

      def redeemWith[R, A, B](fa: RIO[R, A])(
        failure: Throwable => RIO[R, B],
        success: A => RIO[R, B]
      ): RIO[R, B] = fa.foldM(failure, success)

      def failed[R, A](e: Throwable): RIO[R, Nothing] = RIO.fail(e)
    }

  implicit val catsRioMonadError: RioMonadError[CatsRIO] =
    new RioMonadError[CatsRIO] {
      def pure[R, A](a: A): CatsRIO[R, A] = CatsRIO.pure(a)

      def >>=[R, A, B](fa: CatsRIO[R, A])(f: A => CatsRIO[R, B]): CatsRIO[R, B] = fa flatMap f

      def >>>[RA, A, RB >: A, B](fa: CatsRIO[RA, A])(next: CatsRIO[RB, B]): CatsRIO[RA, B] =
        fa >>> next

      def <&>[R, A, B](fa: CatsRIO[R, A])(f: A => B): CatsRIO[R, B] = fa map f

      def redeemWith[R, A, B](fa: CatsRIO[R, A])(
        failure: Throwable => CatsRIO[R, B],
        success: A => CatsRIO[R, B]
      ): CatsRIO[R, B] =
        MonadError[CatsRIO[R, *], Throwable].redeemWith(fa)(failure, success)

      def failed[R, A](e: Throwable): CatsRIO[R, Nothing] =
        MonadError[CatsRIO[R, *], Throwable].raiseError(e)
    }
}
