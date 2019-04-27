package docker
package effect
package syntax

import docker.effect.interop.Provider
import docker.effect.syntax.ProviderSyntax.ProviderOps

import scala.language.implicitConversions

private[syntax] trait ProviderSyntax {
  implicit def ProviderSyntax[R, E, A, F[- _, + _, + _]](fa: F[R, E, A]): ProviderOps[R, E, A, F] =
    new ProviderOps(fa)
}

private[syntax] object ProviderSyntax {

  final class ProviderOps[R, E, A, F[- _, + _, + _]](private val fa: F[R, E, A]) extends AnyVal {
    def provided(r: =>R)(implicit ev: Provider[F]): F[Any, E, A] = ev.provided(fa)(r)
  }
}
