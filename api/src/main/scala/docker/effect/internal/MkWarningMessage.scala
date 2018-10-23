package docker
package effect
package internal

import cats.Show
import docker.effect.types.WarningMessage
import docker.effect.util.CirceCodecs.{ stringDecoderFor, stringEncoderFor }
import io.circe.{ Decoder, Encoder }

object MkWarningMessage extends newtype[String] with WarningMessageInstances with WarningMessageCodecs

sealed private[internal] trait WarningMessageCodecs {

  implicit val warningMessageDecoder: Decoder[WarningMessage] =
    stringDecoderFor(s => WarningMessage(s))

  implicit val warningMessageEncoder: Encoder[WarningMessage] =
    stringEncoderFor[WarningMessage]
}

sealed private[internal] trait WarningMessageInstances {

  implicit val showWarningMessage: Show[WarningMessage] =
    new Show[WarningMessage] {
      def show(t: WarningMessage): String = t.unMk
    }
}
