package docker
package effect
package types

import docker.effect.internal.{ MkImageId, MkImageName, MkImageRepo, MkImageTag }

object Image {

  final val Id = MkImageId
  final type Id = Id.T

  final val Repo = MkImageRepo
  final type Repo = Repo.T

  final val Name = MkImageName
  final type Name = Name.T

  final val Tag = MkImageTag
  final type Tag = Tag.T
}
