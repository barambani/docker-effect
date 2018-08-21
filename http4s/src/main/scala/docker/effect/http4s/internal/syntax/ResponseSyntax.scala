package docker.effect.http4s
package internal
package syntax

import cats.MonadError
import cats.data.EitherT
import cats.syntax.applicativeError._
import docker.effect.types.ErrorMessage
import org.http4s.{EntityDecoder, Response}

import scala.language.implicitConversions

private[syntax] trait ResponseSyntax {

  implicit def responseSyntax[F[_]](response: F[Response[F]]): ResponseOps[F] =
    new ResponseOps(response)
}

final private[syntax] class ResponseOps[F[_]](private val response: F[Response[F]]) extends AnyVal {

  import org.http4s.Status.Ok

  def handleFor[A](
    implicit
      ev1: EntityDecoder[F, A],
      ev2: EntityDecoder[F, ErrorMessage],
      ev3: MonadError[F, Throwable]): EitherT[F, ErrorMessage, A] =
    mapRequestError(response) flatMap {
      case Ok(r) => EitherT.right[ErrorMessage](r.as[A])
      case other => EitherT.left[A](other.as[ErrorMessage])
    }

  private def mapRequestError[A](fa: F[A])(implicit ev: MonadError[F, Throwable]): EitherT[F, ErrorMessage, A] =
    fa.attemptT.leftMap(th => ErrorMessage(s"Request exception: $th"))
}