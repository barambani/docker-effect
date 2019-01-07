package docker
package effect
package algebra
package evidences

sealed trait CompactOptionAllowed[C, O]

object CompactOptionAllowed {

  implicit val evCo1: images -| a  = _evidenceOf[images, a]
  implicit val evCo2: images -| f  = _evidenceOf[images, f]
  implicit val evCo3: images -| q  = _evidenceOf[images, q]
  implicit val evCo4: images -| aq = _evidenceOf[images, aq]

  implicit val evCo5: ps -| a  = _evidenceOf[ps, a]
  implicit val evCo6: ps -| f  = _evidenceOf[ps, f]
  implicit val evCo7: ps -| f  = _evidenceOf[ps, f]
  implicit val evCo8: ps -| l  = _evidenceOf[ps, l]
  implicit val evCo9: ps -| q  = _evidenceOf[ps, q]
  implicit val evCo10: ps -| s = _evidenceOf[ps, s]

  implicit val evCo11: kill -| s = _evidenceOf[kill, s]

  private[this] def _evidenceOf[A, B]: A -| B = new (A -| B) {}
}
