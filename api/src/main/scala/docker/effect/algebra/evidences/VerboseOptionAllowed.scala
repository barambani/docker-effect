package docker.effect
package algebra
package evidences

import scala.annotation.nowarn

sealed trait VerboseOption[_]

@nowarn("msg=parameter value evidence\\$\\d+ in method \\w+ is never used")
object VerboseOption {
  implicit val voEv0: VerboseOption[all]        = _isVerboseOption[all]
  implicit val voEv1: VerboseOption[digest]     = _isVerboseOption[digest]
  implicit val voEv2: VerboseOption[detach]     = _isVerboseOption[detach]
  implicit val voEv3: VerboseOption[filter]     = _isVerboseOption[filter]
  implicit val voEv4: VerboseOption[force]      = _isVerboseOption[force]
  implicit val voEv5: VerboseOption[format]     = _isVerboseOption[format]
  implicit val voEv6: VerboseOption[`no-trunc`] = _isVerboseOption[`no-trunc`]
  implicit val voEv7: VerboseOption[quiet]      = _isVerboseOption[quiet]
  implicit val voEv8: VerboseOption[latest]     = _isVerboseOption[latest]
  implicit val voEv9: VerboseOption[size]       = _isVerboseOption[size]
  implicit val voEv10: VerboseOption[signal]    = _isVerboseOption[signal]

  implicit def voEvT2[A: VerboseOption, B: VerboseOption]: VerboseOption[(A, B)] =
    _isVerboseOption[(A, B)]
  implicit def voEvT3[A: VerboseOption, B: VerboseOption, C: VerboseOption]: VerboseOption[(A, B, C)] =
    _isVerboseOption[(A, B, C)]

  private[this] def _isVerboseOption[A]: VerboseOption[A] = new VerboseOption[A] {}
}

sealed trait VerboseOptionAllowed[Cmd, Opt]

@nowarn("msg=parameter value evidence\\$\\d+ in method \\w+ is never used")
object VerboseOptionAllowed {
  implicit val evLo1: images AcceptsVerboseOpt all        = _isAllowed[images, all]
  implicit val evLo2: images AcceptsVerboseOpt digest     = _isAllowed[images, digest]
  implicit val evLo3: images AcceptsVerboseOpt filter     = _isAllowed[images, filter]
  implicit val evLo4: images AcceptsVerboseOpt format     = _isAllowed[images, format]
  implicit val evLo5: images AcceptsVerboseOpt `no-trunc` = _isAllowed[images, `no-trunc`]
  implicit val evLo6: images AcceptsVerboseOpt quiet      = _isAllowed[images, quiet]

  implicit def evVoT2_1[A: VerboseOption, B: VerboseOption]: images AcceptsVerboseOpt (A, B) =
    _isAllowed[images, (A, B)]
  implicit def evVoT3_1[
    A: VerboseOption,
    B: VerboseOption,
    C: VerboseOption
  ]: images AcceptsVerboseOpt (A, B, C) =
    _isAllowed[images, (A, B, C)]

  implicit val evLo7: ps AcceptsVerboseOpt all         = _isAllowed[ps, all]
  implicit val evLo8: ps AcceptsVerboseOpt filter      = _isAllowed[ps, filter]
  implicit val evLo9: ps AcceptsVerboseOpt format      = _isAllowed[ps, format]
  implicit val evLo10: ps AcceptsVerboseOpt last       = _isAllowed[ps, last]
  implicit val evLo11: ps AcceptsVerboseOpt latest     = _isAllowed[ps, latest]
  implicit val evLo12: ps AcceptsVerboseOpt `no-trunc` = _isAllowed[ps, `no-trunc`]
  implicit val evLo13: ps AcceptsVerboseOpt quiet      = _isAllowed[ps, quiet]
  implicit val evLo14: ps AcceptsVerboseOpt size       = _isAllowed[ps, size]

  implicit def evVoT2_2[A: VerboseOption, B: VerboseOption]: ps AcceptsVerboseOpt (A, B) =
    _isAllowed[ps, (A, B)]
  implicit def evVoT3_2[
    A: VerboseOption,
    B: VerboseOption,
    C: VerboseOption
  ]: ps AcceptsVerboseOpt (A, B, C) =
    _isAllowed[ps, (A, B, C)]

  implicit val evLo15: kill AcceptsVerboseOpt signal = _isAllowed[kill, signal]

  implicit val evLo16: run AcceptsVerboseOpt detach = _isAllowed[run, detach]

  implicit val evLo17: rm AcceptsVerboseOpt force = _isAllowed[rm, force]

  private[this] def _isAllowed[A, B]: A AcceptsVerboseOpt B = new (A AcceptsVerboseOpt B) {}
}
