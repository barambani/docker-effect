package docker.effect
package algebra
package evidences

sealed trait Command[_]
object Command {
  implicit val cmdEv0: Command[container] = _isCommand[container]
  implicit val cmdEv1: Command[images]    = _isCommand[images]
  implicit val cmdEv2: Command[kill]      = _isCommand[kill]
  implicit val cmdEv3: Command[ps]        = _isCommand[ps]
  implicit val cmdEv4: Command[pull]      = _isCommand[pull]
  implicit val cmdEv5: Command[rm]        = _isCommand[rm]
  implicit val cmdEv6: Command[rmi]       = _isCommand[rmi]
  implicit val cmdEv7: Command[run]       = _isCommand[run]
  implicit val cmdEv8: Command[stop]      = _isCommand[stop]

  private[this] def _isCommand[A]: Command[A] = new Command[A] {}
}

sealed trait CommandAllowed[In, Cmd]
object CommandAllowed {
  implicit def initialCommandEv[A: IsInitial, B: Command]: B CmdCanFollow A =
    new (B CmdCanFollow A) {}
}
