package docker.effect
package syntax

import docker.effect.algebra.DockerCommand
import docker.effect.syntax.DockerCommandSyntax.DockerCommandOps

import scala.language.implicitConversions

private[syntax] trait DockerCommandSyntax {
  implicit def dockerCommandSyntax(c: DockerCommand): DockerCommandOps =
    new DockerCommandOps(c)
}

private[syntax] object DockerCommandSyntax {
  final class DockerCommandOps(private val c: DockerCommand) extends AnyVal {
    def words: List[String] = c.unMk.value.split(" ").toList
  }
}
