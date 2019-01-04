package docker
package effect
package algebra
package evidences

import docker.effect.algebra.algebra._

sealed trait Command[_]

object Command {
  implicit val cmdEv0: Command[container] = _evidenceOf[container]
  implicit val cmdEv1: Command[images]    = _evidenceOf[images]
  implicit val cmdEv2: Command[run]       = _evidenceOf[run]
  implicit val cmdEv3: Command[stop]      = _evidenceOf[stop]
  implicit val cmdEv4: Command[kill]      = _evidenceOf[kill]
  implicit val cmdEv5: Command[rm]        = _evidenceOf[rm]
  implicit val cmdEv6: Command[rmi]       = _evidenceOf[rmi]
  implicit val cmdEv7: Command[ps]        = _evidenceOf[ps]

  private[this] def _evidenceOf[A]: Command[A] = new Command[A] {}
}
