package docker
package effect

import docker.effect.algebra.{ DockerCommand, ErrorMessage, SuccessMessage }
import scalaz.zio.ZIO

sealed trait Exec[F[- _, + _, + _]] {
  def run: F[DockerCommand, ErrorMessage, SuccessMessage]
}

object Exec {

  implicit val zioExec: Exec[ZIO] =
    new Exec[ZIO] {
      def run: ZIO[DockerCommand, ErrorMessage, SuccessMessage] = ???
    }
}
