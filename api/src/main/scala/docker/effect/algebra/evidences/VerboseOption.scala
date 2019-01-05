package docker
package effect
package algebra
package evidences

sealed trait VerboseOption[_]

object VerboseOption {
  implicit val voEv0: VerboseOption[all]        = _evidenceOf[all]
  implicit val voEv1: VerboseOption[digest]     = _evidenceOf[digest]
  implicit val voEv2: VerboseOption[filter]     = _evidenceOf[filter]
  implicit val voEv3: VerboseOption[`no-trunc`] = _evidenceOf[`no-trunc`]
  implicit val voEv4: VerboseOption[quiet]      = _evidenceOf[quiet]
  implicit val voEv5: VerboseOption[latest]     = _evidenceOf[latest]
  implicit val voEv6: VerboseOption[size]       = _evidenceOf[size]
  implicit val voEv7: VerboseOption[signal]     = _evidenceOf[signal]

  private[this] def _evidenceOf[A]: VerboseOption[A] = new VerboseOption[A] {}
}
