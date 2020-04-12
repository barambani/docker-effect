package docker.effect
package algebra
package evidences

sealed trait OptionArgumentAllowed[Opt, Arg]

object OptionArgumentAllowed {
  implicit val evOa1: signal AcceptsArgument KILL = _isAllowed[signal, KILL]
  implicit val evOa2: signal AcceptsArgument HUP  = _isAllowed[signal, HUP]

  implicit val ev1a1: s AcceptsArgument KILL = _isAllowed[s, KILL]
  implicit val ev1a2: s AcceptsArgument HUP  = _isAllowed[s, HUP]

  private[this] def _isAllowed[A, B]: A AcceptsArgument B = new (A AcceptsArgument B) {}
}
