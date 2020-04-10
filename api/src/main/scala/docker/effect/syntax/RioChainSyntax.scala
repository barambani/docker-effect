package docker.effect
package syntax

import docker.effect.interop.RioChain
import docker.effect.syntax.RioChainSyntax.RioChainOps

import scala.language.implicitConversions

private[syntax] trait RioChainSyntax {
  implicit def rioChainSyntax[F[-_, +_], R, A](fa: F[R, A]): RioChainOps[F, R, A] =
    new RioChainOps(fa)
}

private[syntax] object RioChainSyntax {
  final class RioChainOps[F[-_, +_], R, A](private val fa: F[R, A]) extends AnyVal {
    def >>>[RB >: A, B](next: F[RB, B])(implicit ev: RioChain[F]): F[R, B] = ev.>>>(fa)(next)
  }
}
