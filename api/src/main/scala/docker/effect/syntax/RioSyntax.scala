package docker.effect
package syntax

import docker.effect.interop.{ RioChain, RioFunctor }
import docker.effect.syntax.RioSyntax.RioOps

import scala.language.implicitConversions

private[syntax] trait RioSyntax {
  implicit def rioSyntax[F[-_, _], R, A](fa: F[R, A]): RioOps[F, R, A] =
    new RioOps(fa)
}

private[syntax] object RioSyntax {
  final class RioOps[F[-_, _], R, A](private val fa: F[R, A]) extends AnyVal {
    def >>>[RB >: A, B](next: F[RB, B])(implicit ev: RioChain[F]): F[R, B] = ev.>>>(fa)(next)

    def <&>[B](f: A => B)(implicit ev: RioFunctor[F]): F[R, B] = ev.<&>(fa)(f)
  }
}
