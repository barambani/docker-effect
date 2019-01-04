package docker
package effect
package algebra
package evidences

import docker.effect.algebra.algebra.docker

sealed trait Initial[_]

object Initial {
  implicit val inEv: Initial[docker] = new Initial[docker] {}
}
