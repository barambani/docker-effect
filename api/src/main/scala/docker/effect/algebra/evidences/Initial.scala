package docker
package effect
package algebra
package evidences

sealed trait Initial[_]

object Initial {
  implicit val inEv: Initial[docker] = new Initial[docker] {}
}
