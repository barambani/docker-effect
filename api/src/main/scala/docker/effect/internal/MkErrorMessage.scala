package docker.effect.internal

import docker.effect.types._
import io.circe.{ Decoder, Encoder }

object MkErrorMessage extends newtype[String] {
  implicit def errorMessageDecoder: Decoder[ErrorMessage] = ???
  implicit def errorMessageEncoder: Encoder[ErrorMessage] = ???
}
