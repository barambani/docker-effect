package docker.effect
package syntax

import docker.effect.interop.Accessor.accessM
import docker.effect.interop.{ Accessor, RioFunctor, RioMonad }
import docker.effect.syntax.RioSyntax.RioOps

import scala.language.implicitConversions

private[syntax] trait RioSyntax {
  implicit def rioSyntax[F[-_, _], R, A](fa: F[R, A]): RioOps[F, R, A] =
    new RioOps(fa)
}

private[syntax] object RioSyntax {
  final class RioOps[F[-_, _], R, A](private val fa: F[R, A]) extends AnyVal {

    def >>=[B](f: A => F[R, B])(implicit ev: RioMonad[F]): F[R, B] = ev.>>=(fa)(f)

    def >>>[RB >: A, B](next: F[RB, B])(implicit ev: RioMonad[F]): F[R, B] = ev.>>>(fa)(next)

    def <&>[B](f: A => B)(implicit ev: RioFunctor[F]): F[R, B] = ev.<&>(fa)(f)

    def flatTap[G[_]](f: A => G[Unit])(implicit m: RioMonad[F], acc: Accessor[G, F]): F[R, A] =
      m.>>=(fa)(a => m.<&>(accessM[R](_ => f(a)))(_ => a))
  }
}
