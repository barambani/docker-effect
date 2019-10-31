package docker
package effect
package algebra
package evidences

sealed trait CommandTargetAllowed[Cmd, Tgt]

object CommandTargetAllowed extends Target2 {
  implicit val evTg1: rmi \\> Id   = _allowed[rmi, Id]
  implicit val evTg2: rmi \\> Repo = _allowed[rmi, Repo]

  implicit val evTg3: kill \\> Name = _allowed[kill, Name]
  implicit val evTg4: kill \\> Id   = _allowed[kill, Id]

  implicit val evTg5: run \\> Name = _allowed[run, Name]
  implicit val evTg6: run \\> Id   = _allowed[run, Id]

  implicit val evTg7: stop \\> Name = _allowed[stop, Name]
  implicit val evTg8: stop \\> Id   = _allowed[stop, Id]

  implicit val evTg9: rm \\> Id    = _allowed[rm, Id]
  implicit val evTg10: rm \\> Name = _allowed[rm, Name]

  implicit val evTg11: pull \\> (Name, Tag) = _allowed[pull, (Name, Tag)]

  implicit def signalEvTg4[Sig: signal =| ?]: Sig \\> Id = _allowed[Sig, Id]
}

sealed private[algebra] trait Target2 {
  implicit def signalEvTg5[Sig: s =| ?]: Sig \\> Id = _allowed[Sig, Id]

  final protected def _allowed[A, B]: A \\> B = new (A \\> B) {}
}
