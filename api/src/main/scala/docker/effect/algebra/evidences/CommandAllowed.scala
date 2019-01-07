package docker
package effect
package algebra
package evidences

sealed trait CommandAllowed[In, Cmd]

object CommandAllowed {
  implicit def initialCommandEv[A: Initial, B: Command]: A :-: B = new (A :-: B) {}
}
