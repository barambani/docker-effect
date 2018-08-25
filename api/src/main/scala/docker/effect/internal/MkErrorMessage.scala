package docker.effect.internal

import cats.Show
import docker.effect.types._
import docker.effect.util.CirceCodecs._
import io.circe.{ Decoder, Encoder }

object MkErrorMessage extends newtype[String] with ErrorMessageInstances with ErrorMessageCodecs

sealed private[internal] trait ErrorMessageCodecs {

  implicit val errorMessageDecoder: Decoder[ErrorMessage] =
    decoderFor(s => ErrorMessage(s))

  implicit val errorMessageEncoder: Encoder[ErrorMessage] =
    encoderFor[ErrorMessage]
}

sealed private[internal] trait ErrorMessageInstances {

  implicit val showErrorMessage: Show[ErrorMessage] =
    new Show[ErrorMessage] {
      def show(t: ErrorMessage): String = t.unMk
    }
}
