package docker
package effect
package algebra
package evidences

import docker.effect.algebra.algebra._

sealed trait VerboseOptionAllowed[Cmd, Opt]

object VerboseOptionAllowed {

  implicit val evLo1: images --| all        = _evidenceOf[images, all]
  implicit val evLo2: images --| digest     = _evidenceOf[images, digest]
  implicit val evLo3: images --| filter     = _evidenceOf[images, filter]
  implicit val evLo4: images --| format     = _evidenceOf[images, format]
  implicit val evLo5: images --| `no-trunc` = _evidenceOf[images, `no-trunc`]
  implicit val evLo6: images --| quiet      = _evidenceOf[images, quiet]

  implicit val evLo7: ps --| all         = _evidenceOf[ps, all]
  implicit val evLo8: ps --| filter      = _evidenceOf[ps, filter]
  implicit val evLo9: ps --| format      = _evidenceOf[ps, format]
  implicit val evLo10: ps --| last       = _evidenceOf[ps, last]
  implicit val evLo11: ps --| latest     = _evidenceOf[ps, latest]
  implicit val evLo12: ps --| `no-trunc` = _evidenceOf[ps, `no-trunc`]
  implicit val evLo13: ps --| quiet      = _evidenceOf[ps, quiet]
  implicit val evLo14: ps --| size       = _evidenceOf[ps, size]

  implicit val evLo15: kill --| signal = _evidenceOf[kill, signal]

  private[this] def _evidenceOf[A, B]: A --| B = new (A --| B) {}
}
