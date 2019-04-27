package docker
package effect

import docker.effect.algebra.{ DockerCommand, ErrorMessage, SuccessMessage }
import scalaz.zio.ZIO

sealed trait Command[F[- _, + _, + _]] {
  def executed: F[DockerCommand, ErrorMessage, SuccessMessage]
}

object Command {

  implicit val zioExec: Command[ZIO] =
    new Command[ZIO] {
      def executed: ZIO[DockerCommand, ErrorMessage, SuccessMessage] = ???
    }
}
