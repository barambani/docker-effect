package docker
package effect
package algebra
package evidences

import docker.effect.algebra.algebra._

sealed trait CompactOptionAllowed[C, O]

object CompactOptionAllowed {

  implicit val evCo1: images -| a = _evidenceOf[images, a]
  implicit val evCo2: images -| f = _evidenceOf[images, f]
  implicit val evCo3: images -| q = _evidenceOf[images, q]

  implicit val evCo7: ps -| a  = _evidenceOf[ps, a]
  implicit val evCo8: ps -| f  = _evidenceOf[ps, f]
  implicit val evCo9: ps -| f  = _evidenceOf[ps, f]
  implicit val evCo10: ps -| l = _evidenceOf[ps, l]
  implicit val evCo11: ps -| q = _evidenceOf[ps, q]
  implicit val evCo12: ps -| s = _evidenceOf[ps, s]

  implicit val evCo13: kill -| s = _evidenceOf[kill, s]

  private[this] def _evidenceOf[A, B]: A -| B = new (A -| B) {}
}
