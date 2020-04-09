package docker.effect
package interop

import cats.Monad

sealed abstract class CovariantKleisli[F[+_]: Monad, -R, +A](val rio: (=>R) => F[A]) {

  @inline final def <&>[B](f: A => B): CovariantKleisli[F, R, B] =
    CovariantKleisli(r => Monad[F].map(rio(r))(f))

  @inline final def >>=[RR <: R, B](f: A => CovariantKleisli[F, RR, B]): CovariantKleisli[F, RR, B] =
    CovariantKleisli(r => Monad[F].flatMap(rio(r))(a => f(a).rio(r)))

  @inline final def >>>[RR >: A, B](next: CovariantKleisli[F, RR, B]): CovariantKleisli[F, R, B] =
    CovariantKleisli(r => Monad[F].flatMap(rio(r))(a => next.rio(a)))

  @inline final def map[B](f: A => B): CovariantKleisli[F, R, B] = <&>(f)

  @inline final def flatMap[RR <: R, B](f: A => CovariantKleisli[F, RR, B]): CovariantKleisli[F, RR, B] = >>=(f)

  @inline final def provided[RR <: R](r: =>RR): F[A] = rio(r)
}

object CovariantKleisli {

  @inline final def apply[F[+_]: Monad, R, A](rio: (=>R) => F[A]): CovariantKleisli[F, R, A] =
    new CovariantKleisli[F, R, A](rio) {}

  @inline final def accessing[F[+_]: Monad, R, A](read: R => CovariantKleisli[F, R, A]): CovariantKleisli[F, R, A] =
    CovariantKleisli(r => read(r).rio(r))
}
