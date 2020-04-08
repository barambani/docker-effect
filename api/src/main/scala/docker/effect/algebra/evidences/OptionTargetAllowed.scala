package docker.effect
package algebra
package evidences

sealed trait OptionTargetAllowed[Opt, Tgt]

object OptionTargetAllowed {
  implicit val opTgtEv1: detach /\> Id   = _isAllowed[detach, Id]
  implicit val opTgtEv2: detach /\> Name = _isAllowed[detach, Name]

  implicit val opTgtEv3: d /\> Id   = _isAllowed[d, Id]
  implicit val opTgtEv4: d /\> Name = _isAllowed[d, Name]

  implicit val opTgtEv5: force /\> Id   = _isAllowed[force, Id]
  implicit val opTgtEv6: force /\> Name = _isAllowed[force, Name]

  implicit val opTgtEv7: f /\> Id   = _isAllowed[f, Id]
  implicit val opTgtEv8: f /\> Name = _isAllowed[f, Name]

  private[this] def _isAllowed[A, B]: A /\> B = new (A /\> B) {}
}
