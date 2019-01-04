package docker.effect.algebra.evidences

import docker.effect.algebra.algebra._

sealed trait CompactOption[_]

object CompactOption {
  implicit val coEv0: CompactOption[a] = _evidenceOf[a]
  implicit val coEv1: CompactOption[f] = _evidenceOf[f]
  implicit val coEv2: CompactOption[q] = _evidenceOf[q]
  implicit val coEv3: CompactOption[l] = _evidenceOf[l]
  implicit val coEv4: CompactOption[s] = _evidenceOf[s]

  private[this] def _evidenceOf[A]: CompactOption[A] = new CompactOption[A] {}
}
