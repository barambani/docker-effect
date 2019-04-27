package docker
package effect
package algebra
package evidences

sealed trait OptionTargetAllowed[Opt, Tgt]

object OptionTargetAllowed {
  implicit val opTgtEv1: detached /\> Id   = _isAllowed[detached, Id]
  implicit val opTgtEv2: detached /\> Name = _isAllowed[detached, Name]

  implicit val opTgtEv3: d /\> Id   = _isAllowed[d, Id]
  implicit val opTgtEv4: d /\> Name = _isAllowed[d, Name]

  private[this] def _isAllowed[A, B]: A /\> B = new (A /\> B) {}
}
