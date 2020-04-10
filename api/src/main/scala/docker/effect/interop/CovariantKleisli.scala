//package docker.effect
//package interop
//
//import cats.{ ApplicativeError, Functor, Monad, MonadError }
//
//import scala.language.implicitConversions
//
//sealed abstract class CovariantKleisli[F[_]: Monad, -R, +A](private[interop] val rio: (=>R) => F[A]) {
//
//  @inline final def <&>[B](f: A => B): CovariantKleisli[F, R, B] =
//    CovariantKleisli(r => Monad[F].map(rio(r))(f))
//
//  @inline final def >>=[RR <: R, B](f: A => CovariantKleisli[F, RR, B]): CovariantKleisli[F, RR, B] =
//    CovariantKleisli(r => Monad[F].flatMap(rio(r))(a => f(a).rio(r)))
//
//  @inline final def >>>[RR >: A, B](next: CovariantKleisli[F, RR, B]): CovariantKleisli[F, R, B] =
//    CovariantKleisli(r => Monad[F].flatMap(rio(r))(a => next.rio(a)))
//
//  @inline final def map[B](f: A => B): CovariantKleisli[F, R, B] = <&>(f)
//
//  @inline final def flatMap[RR <: R, B](f: A => CovariantKleisli[F, RR, B]): CovariantKleisli[F, RR, B] =
//    >>=(f)
//
//  @inline final def redeem[RR <: R, B](err: Throwable => B, succ: A => B)(
//    implicit F: ApplicativeError[F, Throwable]
//  ): CovariantKleisli[F, RR, B] =
//    CovariantKleisli(r => F.redeem(rio(r))(err, succ))
//
//  @inline final def redeemWith[RR <: R, B](err: Throwable => F[B], succ: A => F[B])(
//    implicit F: MonadError[F, Throwable]
//  ): CovariantKleisli[F, RR, B] =
//    CovariantKleisli(r => F.redeemWith(rio(r))(err, succ))
//
//  @inline final def provided[RR <: R](r: =>RR): F[A] = rio(r)
//}
//
//object CovariantKleisli extends CatsRIOFunctions {
//
//  def run[F[_], R, A](ck: CovariantKleisli[F, R, A])(r: R): F[A] = ck.rio(r)
//
//  @inline final def apply[F[_]: Monad, R, A](rio: (=>R) => F[A]): CovariantKleisli[F, R, A] =
//    new CovariantKleisli[F, R, A](rio) {}
//
//  @inline final def unit[F[_]: Monad]: CovariantKleisli[F, Any, Unit] =
//    CovariantKleisli[F, Any, Unit](_ => Monad[F].unit)
//
//  @inline final def accessing[F[_]: Monad, R, A](
//    read: R => CovariantKleisli[F, R, A]
//  ): CovariantKleisli[F, R, A] =
//    CovariantKleisli(r => read(r).rio(r))
//
//  implicit def covariantKleisliFunctor[F[_], R]: Functor[CovariantKleisli[F, R, *]] =
//    new Functor[CovariantKleisli[F, R, *]] {
//      def map[A, B](fa: CovariantKleisli[F, R, A])(f: A => B): CovariantKleisli[F, R, B] = fa <&> f
//    }
//}
//
//private[interop] trait CatsRIOFunctions extends CatsIOFunctions {
//  implicit def catsIOFunctions[F[_], A](c: CovariantKleisli[F, Any, A]): CovariantKleisliAnyOps[F, A] =
//    new CovariantKleisliAnyOps(c)
//}
//
//object CovariantKleisliAny {
//  def run[F[_], A](ck: CovariantKleisli[F, Any, A]): F[A] = ck.rio(())
//}
//
//sealed private[interop] trait CatsIOFunctions {
//  implicit def catsRIOFunctions[F[_], R, A](cr: CovariantKleisli[F, R, A]): CovariantKleisliOps[F, R, A] =
//    new CovariantKleisliOps(cr)
//}
//
//final private[interop] class CovariantKleisliAnyOps[F[_], A](private val c: CovariantKleisli[F, Any, A])
//    extends AnyVal {
//  def run: F[A] = CovariantKleisliAny.run(c)
//}
//
//final private[interop] class CovariantKleisliOps[F[_], R, A](private val cr: CovariantKleisli[F, R, A])
//    extends AnyVal {
//  def run(r: R): F[A] = CovariantKleisli.run(cr)(r)
//}
