package docker.effect
package algebra
package evidences

sealed trait IsInitial[_]

object IsInitial {
  implicit val inEv: IsInitial[docker] = new IsInitial[docker] {}
}
