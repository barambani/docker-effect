package docker.effect.internal

import cats.Show
import docker.effect.types.Image
import docker.effect.util.CirceCodecs.stringEncoderFor
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Encoder

object MkImageRepo extends newtype[NonEmptyString] with ImageRepoInstances with ImageRepoCodecs

sealed private[internal] trait ImageRepoCodecs {

  implicit val imageRepoEncoder: Encoder[Image.Repo] =
    stringEncoderFor[Image.Repo]
}

sealed private[internal] trait ImageRepoInstances {

  implicit val showImageRepo: Show[Image.Repo] =
    new Show[Image.Repo] {
      def show(t: Image.Repo): String = t.unMk.value
    }
}
