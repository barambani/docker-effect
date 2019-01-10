package docker
package effect
package algebra
package evidences

sealed trait CompactOption[_]
object CompactOption {

  implicit val coEv0: CompactOption[a]  = _isCompactOption[a]
  implicit val coEv1: CompactOption[f]  = _isCompactOption[f]
  implicit val coEv2: CompactOption[q]  = _isCompactOption[q]
  implicit val coEv3: CompactOption[l]  = _isCompactOption[l]
  implicit val coEv4: CompactOption[s]  = _isCompactOption[s]
  implicit val coEv5: CompactOption[aq] = _isCompactOption[aq]

  private[this] def _isCompactOption[A]: CompactOption[A] = new CompactOption[A] {}
}

sealed trait CompactOptionAllowed[C, O]
object CompactOptionAllowed {

  implicit val evCo1: images -| a  = _isAllowed[images, a]
  implicit val evCo2: images -| f  = _isAllowed[images, f]
  implicit val evCo3: images -| q  = _isAllowed[images, q]
  implicit val evCo4: images -| aq = _isAllowed[images, aq]

  implicit val evCo5: ps -| a  = _isAllowed[ps, a]
  implicit val evCo6: ps -| f  = _isAllowed[ps, f]
  implicit val evCo7: ps -| f  = _isAllowed[ps, f]
  implicit val evCo8: ps -| l  = _isAllowed[ps, l]
  implicit val evCo9: ps -| q  = _isAllowed[ps, q]
  implicit val evCo10: ps -| s = _isAllowed[ps, s]

  implicit val evCo11: kill -| s = _isAllowed[kill, s]

  private[this] def _isAllowed[A, B]: A -| B = new (A -| B) {}
}
