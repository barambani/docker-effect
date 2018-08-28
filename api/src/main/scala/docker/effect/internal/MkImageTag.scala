package docker.effect.internal

import eu.timepit.refined.types.string.NonEmptyString

object MkImageTag extends newtype[NonEmptyString]
