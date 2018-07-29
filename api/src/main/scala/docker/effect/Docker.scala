package docker.effect

import docker.effect.types.{ |, Container, ErrorMessage }

private[effect] trait Docker[F[_]] {

  def createDefault: (Container.Name, Container.Image) => F[ErrorMessage | Container.Created]
}
