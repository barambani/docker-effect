package docker
package effect

import docker.effect.types.{ |, Container, ErrorMessage, Image }

trait Docker[F[_, _]] {

  def runContainer: Image.Name => F[ErrorMessage, Container.Created]
  def stopContainer: Container.Id | Container.Name => F[ErrorMessage, Unit]
  def killContainer: Container.Id | Container.Name => F[ErrorMessage, Unit]
  def removeContainer: Container.Id | Container.Name => F[ErrorMessage, Unit]
  def forceRemoveContainer: Container.Id | Container.Name => F[ErrorMessage, Unit]
  def removeAllContainers: Unit => F[ErrorMessage, Unit]

  def pullImage: (Image.Name, Image.Tag) => F[ErrorMessage, Unit]
  def listImages: Unit => F[ErrorMessage, Container.Created]
  def removeImage: Image.Id | Image.Name => F[ErrorMessage, Unit]
}
