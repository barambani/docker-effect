package docker.effect
package algebra
package evidences

sealed trait OptionTargetAllowed[Opt, Tgt]

object OptionTargetAllowed {
  implicit val opTgtEv1: detach AcceptsOptTarget Id           = _isAllowed[detach, Id]
  implicit val opTgtEv2: detach AcceptsOptTarget Image        = _isAllowed[detach, Image]
  implicit val opTgtEv3: detach AcceptsOptTarget (Image, Tag) = _isAllowed[detach, (Image, Tag)]

  implicit val opTgtEv4: d AcceptsOptTarget Id           = _isAllowed[d, Id]
  implicit val opTgtEv5: d AcceptsOptTarget Image        = _isAllowed[d, Image]
  implicit val opTgtEv6: d AcceptsOptTarget (Image, Tag) = _isAllowed[d, (Image, Tag)]

  implicit val opTgtEv7: force AcceptsOptTarget Id   = _isAllowed[force, Id]
  implicit val opTgtEv8: force AcceptsOptTarget Name = _isAllowed[force, Name]

  implicit val opTgtEv9: f AcceptsOptTarget Id    = _isAllowed[f, Id]
  implicit val opTgtEv10: f AcceptsOptTarget Name = _isAllowed[f, Name]

  private[this] def _isAllowed[A, B]: A AcceptsOptTarget B = new A AcceptsOptTarget B {}
}
