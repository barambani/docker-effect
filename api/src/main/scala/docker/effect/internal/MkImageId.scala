package docker.effect.internal

import docker.effect.types.Image
import docker.effect.util.CirceCodecs.stringMappedDecoderWithError
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Decoder

object MkImageId extends newtype[NonEmptyString] with ImageIdCodecs

sealed private[internal] trait ImageIdCodecs {

  implicit val imageIdDecoder: Decoder[Image.Id] =
    stringMappedDecoderWithError(
      NonEmptyString.from,
      Image.Id.apply,
      _ => "Cannot decode an empty string to an image id"
    )
}
