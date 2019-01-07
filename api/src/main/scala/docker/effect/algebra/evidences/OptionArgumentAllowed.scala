package docker
package effect
package algebra
package evidences

sealed trait OptionArgumentAllowed[Opt, Arg]

object OptionArgumentAllowed {
  implicit val evOa1: signal =| KILL = _evidenceOf[signal, KILL]
  implicit val evOa2: signal =| HUP  = _evidenceOf[signal, HUP]

  implicit val evOa3: s =| KILL = _evidenceOf[s, KILL]
  implicit val evOa4: s =| HUP  = _evidenceOf[s, HUP]

  private[this] def _evidenceOf[A, B]: A =| B = new (A =| B) {}
}
