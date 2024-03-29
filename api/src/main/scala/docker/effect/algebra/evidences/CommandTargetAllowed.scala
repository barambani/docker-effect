package docker.effect
package algebra
package evidences

import scala.annotation.nowarn

sealed trait CommandTargetAllowed[Cmd, Tgt]

object CommandTargetAllowed extends Target2 {
  implicit val evTg1: rmi AcceptsCmdTarget Id          = _allowed[rmi, Id]
  implicit val evTg2: rmi AcceptsCmdTarget Repo        = _allowed[rmi, Repo]
  implicit val evTg3: rmi AcceptsCmdTarget (Repo, Tag) = _allowed[rmi, (Repo, Tag)]

  implicit val evTg4: kill AcceptsCmdTarget Name = _allowed[kill, Name]
  implicit val evTg5: kill AcceptsCmdTarget Id   = _allowed[kill, Id]

  implicit val evTg6: run AcceptsCmdTarget Image = _allowed[run, Image]
  implicit val evTg7: run AcceptsCmdTarget Id    = _allowed[run, Id]

  implicit val evTg8: stop AcceptsCmdTarget Name = _allowed[stop, Name]
  implicit val evTg9: stop AcceptsCmdTarget Id   = _allowed[stop, Id]

  implicit val evTg10: rm AcceptsCmdTarget Id   = _allowed[rm, Id]
  implicit val evTg11: rm AcceptsCmdTarget Name = _allowed[rm, Name]

  implicit val evTg12: pull AcceptsCmdTarget (Image, Tag) = _allowed[pull, (Image, Tag)]
  implicit val evTg13: run AcceptsCmdTarget (Image, Tag)  = _allowed[run, (Image, Tag)]

  @nowarn("msg=parameter value evidence\\$\\d+ in method signalEvTg4 is never used")
  implicit def signalEvTg4[Sig: signal AcceptsArgument *]: Sig AcceptsCmdTarget Id = _allowed[Sig, Id]
}

private[algebra] sealed trait Target2 {
  @nowarn("msg=parameter value evidence\\$\\d+ in method signalEvTg5 is never used")
  implicit def signalEvTg5[Sig: s AcceptsArgument *]: Sig AcceptsCmdTarget Id = _allowed[Sig, Id]

  protected final def _allowed[A, B]: A AcceptsCmdTarget B = new (A AcceptsCmdTarget B) {}
}
