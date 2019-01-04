package docker
package effect
package algebra
package evidences

import docker.effect.algebra.algebra.docker

sealed trait Init[_]

object Init {
  implicit val inEv: Init[docker] = new Init[docker] {}
}
