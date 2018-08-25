package docker.effect.internal

import cats.Show
import docker.effect.types.WarningMessage
import docker.effect.util.CirceCodecs.{ decoderFor, encoderFor }
import io.circe.{ Decoder, Encoder }

object MkWarningMessage
    extends newtype[String]
    with WarningMessageInstances
    with WarningMessageCodecs

sealed private[internal] trait WarningMessageCodecs {

  implicit val warningMessageDecoder: Decoder[WarningMessage] =
    decoderFor(s => WarningMessage(s))

  implicit val warningMessageEncoder: Encoder[WarningMessage] =
    encoderFor[WarningMessage]
}

sealed private[internal] trait WarningMessageInstances {

  implicit val showWarningMessage: Show[WarningMessage] =
    new Show[WarningMessage] {
      def show(t: WarningMessage): String = t.unMk
    }
}
