package docker.effect
package syntax

import docker.effect.interop.Accessor.accessM
import docker.effect.interop.{ Accessor, RioFunctor, RioMonad, RioMonadError }
import docker.effect.syntax.RioSyntax.RioOps

import scala.language.implicitConversions

private[syntax] trait RioSyntax {
  implicit def rioSyntax[F[-_, +_], R, A](fa: F[R, A]): RioOps[F, R, A] =
    new RioOps(fa)
}

private[syntax] object RioSyntax {
  final class RioOps[F[-_, +_], R, A](private val fa: F[R, A]) extends AnyVal {

    @inline def >>=[B](f: A => F[R, B])(implicit ev: RioMonad[F]): F[R, B] = ev.>>=(fa)(f)

    @inline def >>>[RB >: A, B](next: F[RB, B])(implicit ev: RioMonad[F]): F[R, B] = ev.>>>(fa)(next)

    @inline def <&>[B](f: A => B)(implicit ev: RioFunctor[F]): F[R, B] = ev.<&>(fa)(f)

    @inline def *>[B](fb: F[R, B])(implicit ev: RioMonad[F]): F[R, B] = ev.>>=(fa)(_ => fb)

    @inline def flatTap[G[_]](f: A => G[Unit])(implicit m: RioMonad[F], acc: Accessor[G, F]): F[R, A] =
      m.>>=(fa)(a => m.<&>(accessM[R](_ => f(a)))(_ => a))

    @inline def redeemWith[B](failure: Throwable => F[R, B], success: A => F[R, B])(
      implicit ev: RioMonadError[F]
    ): F[R, B] = ev.redeemWith(fa)(failure, success)
  }
}
