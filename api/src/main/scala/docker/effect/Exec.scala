package docker
package effect

import docker.effect.algebra.{ DockerCommand, ErrorMessage, SuccessMessage }
import scalaz.zio

sealed trait Exec[F[_, _]] {
  def run: DockerCommand => F[ErrorMessage, SuccessMessage]
}

object Exec {

  implicit val zioExec: Exec[zio.IO] =
    new Exec[zio.IO] {
      def run: DockerCommand => zio.IO[ErrorMessage, SuccessMessage] = ???
    }

  implicit val catsEffectExec: Exec[CatsBio] =
    new Exec[CatsBio] {
      def run: DockerCommand => CatsBio[ErrorMessage, SuccessMessage] = ???
    }
}
