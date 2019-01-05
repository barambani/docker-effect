package docker
package effect
package algebra
package evidences

sealed trait CommandAllowed[In, Cmd]

object CommandAllowed {
  implicit def cmdEv[A: Initial, B: Command]: A :-: B = new (A :-: B) {}
}
