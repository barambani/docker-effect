package docker.effect
package interop

import cats.{ ApplicativeError, Monad, MonadError }

import scala.language.implicitConversions

sealed abstract class CovariantKleisli[F[+_]: Monad, -R, +A](private[interop] val rio: R => F[A]) {

  @inline final def <&>[B](f: A => B): CovariantKleisli[F, R, B] =
    CovariantKleisli(r => Monad[F].map(rio(r))(f))

  @inline final def >>=[RR <: R, B](f: A => CovariantKleisli[F, RR, B]): CovariantKleisli[F, RR, B] =
    CovariantKleisli(r => Monad[F].flatMap(rio(r))(a => f(a).rio(r)))

  @inline final def >>>[RR >: A, B](next: CovariantKleisli[F, RR, B]): CovariantKleisli[F, R, B] =
    CovariantKleisli(r => Monad[F].flatMap(rio(r))(a => next.rio(a)))

  @inline final def map[B](f: A => B): CovariantKleisli[F, R, B] = <&>(f)

  @inline final def flatMap[RR <: R, B](f: A => CovariantKleisli[F, RR, B]): CovariantKleisli[F, RR, B] =
    >>=(f)

  @inline final def redeem[RR <: R, E, B](err: E => B, succ: A => B)(
    implicit F: ApplicativeError[F, E]
  ): CovariantKleisli[F, RR, B] =
    CovariantKleisli(r => F.redeem(rio(r))(err, succ))

  @inline final def redeemWith[RR <: R, E, B](err: E => F[B], succ: A => F[B])(
    implicit F: MonadError[F, E]
  ): CovariantKleisli[F, RR, B] =
    CovariantKleisli(r => F.redeemWith(rio(r))(err, succ))

  @inline final def given[RR <: R](r: =>RR): F[A] = rio(r)
}

object CovariantKleisli
    extends CovariantKleisliConstruction
    with CovariantKleisliApplication
    with CovariantKleisliInstances
    with CovariantKleisliSyntax

sealed private[interop] trait CovariantKleisliConstruction {
  @inline final def apply[F[+_]: Monad, R, A](rio: R => F[A]): CovariantKleisli[F, R, A] =
    new CovariantKleisli[F, R, A](rio) {}

  @inline final def pure[F[+_]: Monad, R, A](a: A): CovariantKleisli[F, R, A] =
    apply(_ => Monad[F].pure(a))

  @inline final def unit[F[+_]: Monad]: CovariantKleisli[F, Any, Unit] =
    CovariantKleisli[F, Any, Unit](_ => Monad[F].unit)

  @inline final def accessing[F[+_]: Monad, R, A](
    read: R => CovariantKleisli[F, R, A]
  ): CovariantKleisli[F, R, A] =
    CovariantKleisli(r => read(r).rio(r))
}

sealed private[interop] trait CovariantKleisliApplication {
  @inline final def run[F[+_], R, A](ck: CovariantKleisli[F, R, A])(r: R): F[A] = ck.rio(r)
}

sealed private[interop] trait CovariantKleisliInstances {
  implicit def covariantKleisliMonadError[F[+_]: MonadError[*[_], E], R, E]: MonadError[CovariantKleisli[F, R, *], E] =
    new MonadError[CovariantKleisli[F, R, *], E] {
      private[this] val F: MonadError[F, E] = implicitly

      def raiseError[A](e: E): CovariantKleisli[F, R, A] =
        CovariantKleisli(_ => F.raiseError(e))

      def handleErrorWith[A](fa: CovariantKleisli[F, R, A])(
        f: E => CovariantKleisli[F, R, A]
      ): CovariantKleisli[F, R, A] =
        CovariantKleisli(r => F.handleErrorWith(fa.rio(r))(f(_).rio(r)))

      def pure[A](x: A): CovariantKleisli[F, R, A] =
        CovariantKleisli.pure(x)

      def flatMap[A, B](fa: CovariantKleisli[F, R, A])(
        f: A => CovariantKleisli[F, R, B]
      ): CovariantKleisli[F, R, B] =
        fa >>= f

      def tailRecM[A, B](a: A)(f: A => CovariantKleisli[F, R, Either[A, B]]): CovariantKleisli[F, R, B] =
        CovariantKleisli(r => F.tailRecM(a)(f(_).rio(r)))
    }
}

sealed private[interop] trait CovariantKleisliSyntax {
  implicit def covariantKleisliApplication[F[+_], R, A](
    cr: CovariantKleisli[F, R, A]
  ): CovariantKleisliSyntaxOps[F, R, A] =
    new CovariantKleisliSyntaxOps(cr)
}

final private[interop] class CovariantKleisliSyntaxOps[F[+_], R, A](private val cr: CovariantKleisli[F, R, A])
    extends AnyVal {
  def run(r: R): F[A] = CovariantKleisli.run(cr)(r)
}
