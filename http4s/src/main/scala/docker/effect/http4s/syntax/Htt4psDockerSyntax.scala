package docker.effect
package http4s
package syntax

import cats.effect.Effect
import cats.syntax.apply._
import cats.syntax.flatMap._

import scala.language.implicitConversions

private[syntax] trait Htt4psDockerSyntax {

  implicit def ioClientSyntax[F[_]](client: F[Http4sDocker[F]]): IoClientOps[F] =
    new IoClientOps(client)

  implicit def clientSyntax[F[_]](client: Http4sDocker[F]): ClientOps[F] =
    new ClientOps(client)
}

final private[syntax] class IoClientOps[F[_]](private val client: F[Http4sDocker[F]])
    extends AnyVal {

  def setup(implicit F: Effect[F]): F[Unit] =
    (client flatMap (cl => Http4sDocker.setup(cl).value)) *> F.unit

  def cleanup(implicit F: Effect[F]): F[Unit] =
    (client flatMap (cl => Http4sDocker.cleanup(cl).value)) *> F.unit
}

final private[syntax] class ClientOps[F[_]](private val client: Http4sDocker[F]) extends AnyVal {

  def setup(implicit F: Effect[F]): F[Unit] =
    Http4sDocker.setup(client).value *> F.unit

  def cleanup(implicit F: Effect[F]): F[Unit] =
    Http4sDocker.cleanup(client).value *> F.unit
}
