package docker
package effect
package algebra
package evidences

sealed trait OptionArgumentAllowed[Opt, Arg]

object OptionArgumentAllowed {
  implicit val evOa1: signal =| KILL = _isAllowed[signal, KILL]
  implicit val evOa2: signal =| HUP  = _isAllowed[signal, HUP]

  implicit val ev1a1: s =| KILL = _isAllowed[s, KILL]
  implicit val ev1a2: s =| HUP  = _isAllowed[s, HUP]

  private[this] def _isAllowed[A, B]: A =| B = new (A =| B) {}
}
