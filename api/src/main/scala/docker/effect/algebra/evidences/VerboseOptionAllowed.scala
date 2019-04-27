package docker
package effect
package algebra
package evidences

sealed trait VerboseOption[_]
object VerboseOption {
  implicit val voEv0: VerboseOption[all]        = _isVerboseOption[all]
  implicit val voEv1: VerboseOption[digest]     = _isVerboseOption[digest]
  implicit val voEv2: VerboseOption[detached]   = _isVerboseOption[detached]
  implicit val voEv3: VerboseOption[filter]     = _isVerboseOption[filter]
  implicit val voEv4: VerboseOption[`no-trunc`] = _isVerboseOption[`no-trunc`]
  implicit val voEv5: VerboseOption[quiet]      = _isVerboseOption[quiet]
  implicit val voEv6: VerboseOption[latest]     = _isVerboseOption[latest]
  implicit val voEv7: VerboseOption[size]       = _isVerboseOption[size]
  implicit val voEv8: VerboseOption[signal]     = _isVerboseOption[signal]

  private[this] def _isVerboseOption[A]: VerboseOption[A] = new VerboseOption[A] {}
}

sealed trait VerboseOptionAllowed[Cmd, Opt]
object VerboseOptionAllowed {

  implicit val evLo1: images --| all        = _isAllowed[images, all]
  implicit val evLo2: images --| digest     = _isAllowed[images, digest]
  implicit val evLo3: images --| filter     = _isAllowed[images, filter]
  implicit val evLo4: images --| format     = _isAllowed[images, format]
  implicit val evLo5: images --| `no-trunc` = _isAllowed[images, `no-trunc`]
  implicit val evLo6: images --| quiet      = _isAllowed[images, quiet]

  implicit val evLo7: ps --| all         = _isAllowed[ps, all]
  implicit val evLo8: ps --| filter      = _isAllowed[ps, filter]
  implicit val evLo9: ps --| format      = _isAllowed[ps, format]
  implicit val evLo10: ps --| last       = _isAllowed[ps, last]
  implicit val evLo11: ps --| latest     = _isAllowed[ps, latest]
  implicit val evLo12: ps --| `no-trunc` = _isAllowed[ps, `no-trunc`]
  implicit val evLo13: ps --| quiet      = _isAllowed[ps, quiet]
  implicit val evLo14: ps --| size       = _isAllowed[ps, size]

  implicit val evLo15: kill --| signal = _isAllowed[kill, signal]

  implicit val evLo16: run --| detached = _isAllowed[run, detached]

  private[this] def _isAllowed[A, B]: A --| B = new (A --| B) {}
}
