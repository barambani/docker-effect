package docker.effect.internal

import cats.Show
import docker.effect.types.Image
import docker.effect.util.CirceCodecs.stringEncoderFor
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Encoder

object MkImageName extends newtype[NonEmptyString] with ImageNameInstances with ImageNameCodecs

sealed private[internal] trait ImageNameCodecs {

  implicit val imageNameEncoder: Encoder[Image.Name] =
    stringEncoderFor[Image.Name]
}

sealed private[internal] trait ImageNameInstances {

  implicit val showImageName: Show[Image.Name] =
    new Show[Image.Name] {
      def show(t: Image.Name): String = t.unMk.value
    }
}
