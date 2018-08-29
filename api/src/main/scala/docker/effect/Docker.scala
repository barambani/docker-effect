package docker.effect

import docker.effect.types.Container.WaitBeforeKill
import docker.effect.types.{ |, Container, ErrorMessage, Image }

trait Docker[F[_, _]] {

  def startSocketRelay: F[ErrorMessage, Unit]
  def cleanSocketRelay: F[ErrorMessage, Unit]

  def createContainer: (Container.Name, Image.Name) => F[ErrorMessage, Container.Created]

  def startContainer: Container.Id | Container.Name => F[ErrorMessage, Unit]

  def stopContainer: Container.Id | Container.Name => F[ErrorMessage, Unit]
  def stopContainerSchedule: (Container.Id | Container.Name, WaitBeforeKill) => F[ErrorMessage, Unit]

  def waitContainerStop: Container.Id | Container.Name => F[ErrorMessage, Unit]

  def restartContainer: Container.Id | Container.Name => F[ErrorMessage, Unit]
  def restartContainerSchedule: (Container.Id | Container.Name, WaitBeforeKill) => F[ErrorMessage, Unit]

  def killContainer: Container.Id | Container.Name => F[ErrorMessage, Unit]

  def removeContainer: Container.Id | Container.Name => F[ErrorMessage, Unit]
  def forceRemoveContainer: Container.Id | Container.Name => F[ErrorMessage, Unit]
  def removeContainerAndVolumes: Container.Id | Container.Name => F[ErrorMessage, Unit]

  def pullImage: (Image.Name, Image.Tag) => F[ErrorMessage, Unit]
  def removeImage: Image.Id | Image.Name => F[ErrorMessage, Unit]
}
