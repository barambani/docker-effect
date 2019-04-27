package docker
package effect
package algebra
package newtypes

import cats.Show
import docker.effect.internal.newtype
import eu.timepit.refined.types.string.NonEmptyString

object MkDockerCommand extends newtype[NonEmptyString] with DockerCommandInstances

sealed private[newtypes] trait DockerCommandInstances {

  implicit val dockerCommandShow: Show[DockerCommand] =
    new Show[DockerCommand] {
      def show(t: DockerCommand): String = t.unMk.value
    }
}
