package docker
package effect
package algebra
package evidences

sealed trait CommandTargetAllowed[Prv, Tgt]

object CommandTargetAllowed extends Target2 {

  implicit val evTg1: rmi \\> Id   = _evidenceOf[rmi, Id]
  implicit val evTg2: rmi \\> Repo = _evidenceOf[rmi, Repo]
  implicit val evTg3: kill \\> Id  = _evidenceOf[kill, Id]

  implicit def evTg4[Sig: signal =| ?]: Sig \\> Id = _evidenceOf[Sig, Id]
}

sealed private[algebra] trait Target2 {

  implicit def evTg5[Sig: s =| ?]: Sig \\> Id = _evidenceOf[Sig, Id]

  final protected def _evidenceOf[A, B]: A \\> B = new (A \\> B) {}
}
