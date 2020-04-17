package docker.effect
package syntax

import docker.effect.interop.Provider
import docker.effect.syntax.ProviderSyntax.{ ProviderOps, UnitProviderOps }

import scala.language.implicitConversions

private[syntax] trait ProviderSyntax {
  implicit def providerSyntax[R, E, A, F[-_, _], G[_]](fa: F[R, A]): ProviderOps[R, A, F, G] =
    new ProviderOps(fa)

  implicit def unitProviderSyntax[E, A, F[-_, _], G[_]](fa: F[Unit, A]): UnitProviderOps[A, F, G] =
    new UnitProviderOps(fa)
}

private[syntax] object ProviderSyntax {
  final class ProviderOps[R, A, F[-_, _], G[_]](private val fa: F[R, A]) extends AnyVal {
    def provided(r: =>R)(implicit ev: Provider[F, G]): G[A] = ev.provided(fa)(r)
  }

  final class UnitProviderOps[A, F[-_, _], G[_]](private val fa: F[Unit, A]) extends AnyVal {
    def providedUnit(implicit ev: Provider[F, G]): G[A] = ev.provided(fa)(())
  }
}
