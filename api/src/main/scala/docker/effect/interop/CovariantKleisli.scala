package docker.effect
package interop

import cats.{ Functor, Monad }

sealed abstract class CovariantKleisli[F[+_]: Monad, -R, +A](val rio: (=>R) => F[A]) {

  def <&>[B](f: A => B): CovariantKleisli[F, R, B] =
    CovariantKleisli(r => Monad[F].map(rio(r))(f))

  def >>=[RR <: R, B](f: A => CovariantKleisli[F, RR, B]): CovariantKleisli[F, RR, B] =
    CovariantKleisli(r => Monad[F].flatMap(rio(r))(a => f(a).rio(r)))

  def >>>[RR >: A, B](next: CovariantKleisli[F, RR, B]): CovariantKleisli[F, R, B] =
    CovariantKleisli(r => Monad[F].flatMap(rio(r))(a => next.rio(a)))

  def map[B](f: A => B): CovariantKleisli[F, R, B] = <&>(f)

  def flatMap[RR <: R, B](f: A => CovariantKleisli[F, RR, B]): CovariantKleisli[F, RR, B] = >>=(f)

  def provided[RR <: R](r: =>RR): F[A] = rio(r)
}

object CovariantKleisli {

  def apply[F[+_]: Monad, R, A](rio: (=>R) => F[A]): CovariantKleisli[F, R, A] =
    new CovariantKleisli[F, R, A](rio) {}

  implicit def covariantKleisliFunctor[F[+_]: Functor, R]: Functor[CovariantKleisli[F, R, *]] =
    new Functor[CovariantKleisli[F, R, *]] {
      def map[A, B](fa: CovariantKleisli[F, R, A])(f: A => B): CovariantKleisli[F, R, B] = fa <&> f
    }

  def accessing[F[+_]: Monad, R, A](read: R => CovariantKleisli[F, R, A]): CovariantKleisli[F, R, A] =
    CovariantKleisli(r => read(r).rio(r))
}
