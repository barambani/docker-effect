package docker.effect
package syntax

import docker.effect.interop.Provider
import docker.effect.syntax.ProviderSyntax.ProviderOps

import scala.language.implicitConversions

private[syntax] trait ProviderSyntax {
  implicit def providerSyntax[R, E, A, F[-_, _], G[_]](fa: F[R, A]): ProviderOps[R, A, F, G] =
    new ProviderOps(fa)
}

private[syntax] object ProviderSyntax {
  final class ProviderOps[R, A, F[-_, _], G[_]](private val fa: F[R, A]) extends AnyVal {
    def provided(r: =>R)(implicit ev: Provider[F, G]): G[A] = ev.provided(fa)(r)
  }
}
