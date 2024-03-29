package docker.effect
package algebra
package evidences

import scala.annotation.nowarn

sealed trait CompactOption[_]

@nowarn("msg=parameter value evidence\\$\\d+ in method \\w+ is never used")
object CompactOption {
  implicit val coEv0: CompactOption[a]  = _isCompactOption[a]
  implicit val coEv1: CompactOption[d]  = _isCompactOption[d]
  implicit val coEv2: CompactOption[f]  = _isCompactOption[f]
  implicit val coEv3: CompactOption[q]  = _isCompactOption[q]
  implicit val coEv4: CompactOption[l]  = _isCompactOption[l]
  implicit val coEv5: CompactOption[s]  = _isCompactOption[s]
  implicit val coEv6: CompactOption[aq] = _isCompactOption[aq]

  implicit def coEvT2[A: CompactOption, B: CompactOption]: CompactOption[(A, B)] =
    _isCompactOption[(A, B)]
  implicit def coEvT3[A: CompactOption, B: CompactOption, C: CompactOption]: CompactOption[(A, B, C)] =
    _isCompactOption[(A, B, C)]

  private[this] def _isCompactOption[A]: CompactOption[A] = new CompactOption[A] {}
}

sealed trait CompactOptionAllowed[C, O]

@nowarn("msg=parameter value evidence\\$\\d+ in method \\w+ is never used")
object CompactOptionAllowed {
  implicit val evCo1: images AcceptsCompactOpt a  = _isAllowed[images, a]
  implicit val evCo2: images AcceptsCompactOpt f  = _isAllowed[images, f]
  implicit val evCo3: images AcceptsCompactOpt q  = _isAllowed[images, q]
  implicit val evCo4: images AcceptsCompactOpt aq = _isAllowed[images, aq]

  implicit def evCoT2_1[A: CompactOption, B: CompactOption]: images AcceptsCompactOpt (A, B) =
    _isAllowed[images, (A, B)]
  implicit def evCoT3_1[
    A: CompactOption,
    B: CompactOption,
    C: CompactOption
  ]: images AcceptsCompactOpt (A, B, C) =
    _isAllowed[images, (A, B, C)]

  implicit val evCo5: ps AcceptsCompactOpt a  = _isAllowed[ps, a]
  implicit val evCo6: ps AcceptsCompactOpt f  = _isAllowed[ps, f]
  implicit val evCo7: ps AcceptsCompactOpt f  = _isAllowed[ps, f]
  implicit val evCo8: ps AcceptsCompactOpt l  = _isAllowed[ps, l]
  implicit val evCo9: ps AcceptsCompactOpt q  = _isAllowed[ps, q]
  implicit val evCo10: ps AcceptsCompactOpt s = _isAllowed[ps, s]

  implicit def evCoT2_2[A: CompactOption, B: CompactOption]: ps AcceptsCompactOpt (A, B) =
    _isAllowed[ps, (A, B)]
  implicit def evCoT3_2[
    A: CompactOption,
    B: CompactOption,
    C: CompactOption
  ]: ps AcceptsCompactOpt (A, B, C) =
    _isAllowed[ps, (A, B, C)]

  implicit val evCo11: kill AcceptsCompactOpt s = _isAllowed[kill, s]

  implicit val evLo12: run AcceptsCompactOpt d = _isAllowed[run, d]

  implicit val evLo13: rm AcceptsCompactOpt f = _isAllowed[rm, f]

  private[this] def _isAllowed[A, B]: A AcceptsCompactOpt B = new (A AcceptsCompactOpt B) {}
}
