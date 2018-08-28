package docker.effect.internal

import eu.timepit.refined.types.string.NonEmptyString

object MkEnginePath extends newtype[NonEmptyString]
