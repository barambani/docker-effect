package docker.effect.internal

import cats.Applicative
import docker.effect.types._
import io.circe.{ Decoder, Encoder }

import scala.language.implicitConversions

object MkErrorMessage extends newtype[String] {
  implicit def errorMessageDecoder: Decoder[ErrorMessage] = ???
  implicit def errorMessageEncoder: Encoder[ErrorMessage] = ???

  implicit def errorMessageSyntax(e: ErrorMessage): ErrorMessageOps = new ErrorMessageOps(e)

  final private[internal] class ErrorMessageOps(private val e: ErrorMessage) extends AnyVal {
    def asPure[F[_]](implicit F: Applicative[F]): F[ErrorMessage] = F.pure(e)
  }
}
