package docker.effect
package http4s
package syntax

import cats.effect.Effect
import cats.syntax.flatMap._
import docker.effect.types.ErrorMessage

import scala.language.implicitConversions

private[syntax] trait Htt4psDockerSyntax {

  implicit def ioClientSyntax[F[_]](client: F[Http4sDocker[F]]): IoClientOps[F] =
    new IoClientOps(client)

  implicit def clientSyntax[F[_]](client: Http4sDocker[F]): ClientOps[F] =
    new ClientOps(client)
}

final private[syntax] class IoClientOps[F[_]](private val client: F[Http4sDocker[F]])
    extends AnyVal {

  def setupUnixSocketRelay(implicit F: Effect[F]): F[Either[ErrorMessage, Unit]] =
    client flatMap (cl => Http4sDocker.setupUnixSocketRelay(cl).value)

  def cleanupUnixSocketRelay(implicit F: Effect[F]): F[Either[ErrorMessage, Unit]] =
    client flatMap (cl => Http4sDocker.cleanupUnixSocketRelay(cl).value)
}

final private[syntax] class ClientOps[F[_]](private val client: Http4sDocker[F]) extends AnyVal {

  def setupUnixSocketRelay(implicit F: Effect[F]): F[Either[ErrorMessage, Unit]] =
    Http4sDocker.setupUnixSocketRelay(client).value

  def cleanupUnixSocketRelay(implicit F: Effect[F]): F[Either[ErrorMessage, Unit]] =
    Http4sDocker.cleanupUnixSocketRelay(client).value
}
