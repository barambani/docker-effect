package docker.effect.internal

import cats.Show
import docker.effect.types._
import io.circe.syntax._
import io.circe.{ Decoder, Encoder, Json }

object MkErrorMessage extends newtype[String] with ErrorMessageInstances with ErrorMessageCodecs

sealed private[internal] trait ErrorMessageCodecs {

  implicit val errorMessageDecoder: Decoder[ErrorMessage] =
    Decoder.instance { c =>
      c.downField("message").as[String] map ErrorMessage.apply
    }

  implicit val errorMessageEncoder: Encoder[ErrorMessage] =
    Encoder.instance { em =>
      Json.fromFields(
        ("message" -> em.unMk.asJson) :: Nil
      )
    }
}

sealed private[internal] trait ErrorMessageInstances {

  implicit val showErrorMessage: Show[ErrorMessage] =
    new Show[ErrorMessage] {
      def show(t: ErrorMessage): String = t.unMk
    }
}
