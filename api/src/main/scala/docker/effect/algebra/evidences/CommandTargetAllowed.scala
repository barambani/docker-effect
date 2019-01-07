package docker
package effect
package algebra
package evidences

sealed trait CommandTargetAllowed[Prv, Tgt]

object CommandTargetAllowed extends Target2 {

  implicit val evTg1: rmi \\> Id   = _evidenceOf[rmi, Id]
  implicit val evTg2: rmi \\> Repo = _evidenceOf[rmi, Repo]
  implicit val evTg3: kill \\> Id  = _evidenceOf[kill, Id]
  implicit val evTg4: run \\> Name = _evidenceOf[run, Name]
  implicit val evTg5: run \\> Id   = _evidenceOf[run, Id]

  implicit def signalEvTg4[Sig: signal =| ?]: Sig \\> Id = _evidenceOf[Sig, Id]
}

sealed private[algebra] trait Target2 {

  implicit def signalEvTg5[Sig: s =| ?]: Sig \\> Id = _evidenceOf[Sig, Id]

  final protected def _evidenceOf[A, B]: A \\> B = new (A \\> B) {}
}
