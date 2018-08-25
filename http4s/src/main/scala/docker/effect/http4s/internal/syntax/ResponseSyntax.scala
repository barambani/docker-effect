package docker.effect.http4s
package internal
package syntax

import cats.MonadError
import cats.data.EitherT
import cats.syntax.applicativeError._
import docker.effect.types.ErrorMessage
import org.http4s.Response

import scala.language.implicitConversions

private[syntax] trait ResponseSyntax {

  implicit def responseSyntax[F[_]](response: F[Response[F]]): ResponseOps[F] =
    new ResponseOps(response)
}

final private[syntax] class ResponseOps[F[_]](private val response: F[Response[F]]) extends AnyVal {

  def handleWith[A](f: Response[F] => EitherT[F, ErrorMessage, A])(
    implicit ev3: MonadError[F, Throwable]
  ): EitherT[F, ErrorMessage, A] =
    mapError(response) flatMap f

  private def mapError[A](fa: F[A])(
    implicit ev: MonadError[F, Throwable]
  ): EitherT[F, ErrorMessage, A] =
    fa.attemptT.leftMap(th => ErrorMessage(s"Request exception: $th"))
}
