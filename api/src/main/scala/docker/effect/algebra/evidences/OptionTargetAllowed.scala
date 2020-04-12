package docker.effect
package algebra
package evidences

sealed trait OptionTargetAllowed[Opt, Tgt]

object OptionTargetAllowed {
  implicit val opTgtEv1: detach AcceptsOptTarget Id   = _isAllowed[detach, Id]
  implicit val opTgtEv2: detach AcceptsOptTarget Name = _isAllowed[detach, Name]

  implicit val opTgtEv3: d AcceptsOptTarget Id   = _isAllowed[d, Id]
  implicit val opTgtEv4: d AcceptsOptTarget Name = _isAllowed[d, Name]

  implicit val opTgtEv5: force AcceptsOptTarget Id   = _isAllowed[force, Id]
  implicit val opTgtEv6: force AcceptsOptTarget Name = _isAllowed[force, Name]

  implicit val opTgtEv7: f AcceptsOptTarget Id   = _isAllowed[f, Id]
  implicit val opTgtEv8: f AcceptsOptTarget Name = _isAllowed[f, Name]

  private[this] def _isAllowed[A, B]: A AcceptsOptTarget B = new (A AcceptsOptTarget B) {}
}
