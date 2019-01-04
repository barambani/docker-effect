package docker
package effect
package algebra

import com.github.ghik.silencer.silent
import docker.effect.algebra.evidences.{ Command, CompactOption, Init, VerboseOption }
import docker.effect.algebra.proofs.{ Printed, Valid }
import docker.effect.internal.newtype
import eu.timepit.refined.W
import eu.timepit.refined.api.{ Refined, RefinedTypeOps }
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.types.string.NonEmptyString
import shapeless._
import shapeless.ops.hlist.Last

object algebra {

  final val ErrorMessage = MkErrorMessage
  final type ErrorMessage = ErrorMessage.T

  final val SuccessMessage = MkSuccessMessage
  final type SuccessMessage = SuccessMessage.T

  //  commands
  final type docker    = String Refined Equal[W.`"docker"`.T]
  final type container = String Refined Equal[W.`"container"`.T]
  final type images    = String Refined Equal[W.`"images"`.T]
  final type run       = String Refined Equal[W.`"run"`.T]
  final type stop      = String Refined Equal[W.`"stop"`.T]
  final type kill      = String Refined Equal[W.`"kill"`.T]
  final type rm        = String Refined Equal[W.`"rm"`.T]
  final type rmi       = String Refined Equal[W.`"rmi"`.T]
  final type ps        = String Refined Equal[W.`"ps"`.T]

  //  options
  final type all        = String Refined Equal[W.`"all"`.T]
  final type digest     = String Refined Equal[W.`"digest"`.T]
  final type filter     = String Refined Equal[W.`"filter"`.T]
  final type format     = String Refined Equal[W.`"format"`.T]
  final type `no-trunc` = String Refined Equal[W.`"no-trunc"`.T]
  final type quiet      = String Refined Equal[W.`"quiet"`.T]
  final type last       = String Refined Equal[W.`"last"`.T]
  final type latest     = String Refined Equal[W.`"latest"`.T]
  final type size       = String Refined Equal[W.`"size"`.T]
  final type signal     = String Refined Equal[W.`"signal"`.T]

  //  compact options
  final type a = String Refined Equal[W.`"a"`.T]
  final type f = String Refined Equal[W.`"f"`.T]
  final type q = String Refined Equal[W.`"q"`.T]
  final type l = String Refined Equal[W.`"l"`.T]
  final type s = String Refined Equal[W.`"s"`.T]

  // signals
  final type KILL = String Refined Equal[W.`"KILL"`.T]
  final type HUP  = String Refined Equal[W.`"HUP"`.T]

  // arguments
  final type Id   = String Refined MatchesRegex[W.`"[0-9a-fA-F]+"`.T]
  final type Name = String Refined MatchesRegex[W.`"[-0-9a-zA-Z]+"`.T]
  final type Repo = String Refined MatchesRegex[W.`"[-0-9a-zA-Z]+"`.T]
  final type Tag  = Tag.T

  final object Id   extends RefinedTypeOps[Id, String]
  final object Name extends RefinedTypeOps[Name, String]
  final object Repo extends RefinedTypeOps[Repo, String]
  final val Tag = newtype[NonEmptyString]

  // relationships
  final type :-:[In, Cmd] = CommandAllowed[In, Cmd]
  sealed trait CommandAllowed[In, Cmd]
  final object CommandAllowed {
    implicit def cmdEv[A: Init, B: Command]: A :-: B = new (A :-: B) {}
  }

  final type --|[Cmd, Opt] = VerboseOptionAllowed[Cmd, Opt]
  sealed trait VerboseOptionAllowed[Cmd, Opt]
  final object VerboseOptionAllowed {

    implicit val evLo1: images --| all        = _evidenceOf[images, all]
    implicit val evLo2: images --| digest     = _evidenceOf[images, digest]
    implicit val evLo3: images --| filter     = _evidenceOf[images, filter]
    implicit val evLo4: images --| format     = _evidenceOf[images, format]
    implicit val evLo5: images --| `no-trunc` = _evidenceOf[images, `no-trunc`]
    implicit val evLo6: images --| quiet      = _evidenceOf[images, quiet]

    implicit val evLo7: ps --| all         = _evidenceOf[ps, all]
    implicit val evLo8: ps --| filter      = _evidenceOf[ps, filter]
    implicit val evLo9: ps --| format      = _evidenceOf[ps, format]
    implicit val evLo10: ps --| last       = _evidenceOf[ps, last]
    implicit val evLo11: ps --| latest     = _evidenceOf[ps, latest]
    implicit val evLo12: ps --| `no-trunc` = _evidenceOf[ps, `no-trunc`]
    implicit val evLo13: ps --| quiet      = _evidenceOf[ps, quiet]
    implicit val evLo14: ps --| size       = _evidenceOf[ps, size]

    implicit val evLo15: kill --| signal = _evidenceOf[kill, signal]

    private[this] def _evidenceOf[A, B]: A --| B = new (A --| B) {}
  }

  final type -|[Cmd, Opt] = CompactOptionAllowed[Cmd, Opt]
  sealed trait CompactOptionAllowed[C, O]
  final object CompactOptionAllowed {

    implicit val evCo1: images -| a = _evidenceOf[images, a]
    implicit val evCo2: images -| f = _evidenceOf[images, f]
    implicit val evCo3: images -| q = _evidenceOf[images, q]

    implicit val evCo7: ps -| a  = _evidenceOf[ps, a]
    implicit val evCo8: ps -| f  = _evidenceOf[ps, f]
    implicit val evCo9: ps -| f  = _evidenceOf[ps, f]
    implicit val evCo10: ps -| l = _evidenceOf[ps, l]
    implicit val evCo11: ps -| q = _evidenceOf[ps, q]
    implicit val evCo12: ps -| s = _evidenceOf[ps, s]

    implicit val evCo13: kill -| s = _evidenceOf[kill, s]

    private[this] def _evidenceOf[A, B]: A -| B = new (A -| B) {}
  }

  final type =|[Opt, Arg] = OptionArgumentAllowed[Opt, Arg]
  sealed trait OptionArgumentAllowed[Opt, Arg]
  final object OptionArgumentAllowed {
    implicit val evOa1: signal =| KILL = _evidenceOf[signal, KILL]
    implicit val evOa2: signal =| HUP  = _evidenceOf[signal, HUP]
    implicit val evOa3: s =| KILL      = _evidenceOf[s, KILL]
    implicit val evOa4: s =| HUP       = _evidenceOf[s, HUP]

    private[this] def _evidenceOf[A, B]: A =| B = new (A =| B) {}
  }

  final type \\>[Prv, Tgt] = CommandTargetAllowed[Prv, Tgt]
  sealed trait CommandTargetAllowed[Prv, Tgt]
  final object CommandTargetAllowed extends Target2 {

    implicit val evTg1: rmi \\> Id   = _evidenceOf[rmi, Id]
    implicit val evTg2: rmi \\> Repo = _evidenceOf[rmi, Repo]
    implicit val evTg3: kill \\> Id  = _evidenceOf[kill, Id]

    implicit def evTg4[Sig: signal =| ?]: Sig \\> Id = _evidenceOf[Sig, Id]
  }

  sealed private[algebra] trait Target2 {

    implicit def evTg5[Sig: s =| ?]: Sig \\> Id = _evidenceOf[Sig, Id]

    protected def _evidenceOf[A, B]: A \\> B = new (A \\> B) {}
  }

  sealed trait CanEndWith[A, B]
  final object CanEndWith extends CanEndWith2 {

    implicit def evCew1[O: VerboseOption: images --| ?]: CanEndWith[images, O] = _evidenceOf[images, O]
    implicit def evCew2[O: VerboseOption: ps --| ?]: CanEndWith[ps, O]         = _evidenceOf[ps, O]

    implicit def targetEv[A, Tgt: A \\> ?]: CanEndWith[A, Tgt] = _evidenceOf[A, Tgt]
  }

  sealed private[algebra] trait CanEndWith2 {

    implicit val evCew1: CanEndWith[docker, images] = _evidenceOf[docker, images]
    implicit val evCew2: CanEndWith[docker, ps]     = _evidenceOf[docker, ps]

    implicit def evCew3[O: CompactOption: images -| ?]: CanEndWith[images, O] = _evidenceOf[images, O]
    implicit def evCew4[O: CompactOption: ps -| ?]: CanEndWith[ps, O]         = _evidenceOf[ps, O]
    implicit def evCew5[O, Arg: O =| ?]: CanEndWith[O, Arg]                   = _evidenceOf[O, Arg]

    final protected def _evidenceOf[A, B]: CanEndWith[A, B] = new CanEndWith[A, B] {}
  }

  // interpreters
  final def print0[Cmd <: HList: Valid](implicit p: Printed[Cmd]): String = p.text

  final def print1[Cmd <: HList]: printPartialTypeApplication[Cmd] =
    new printPartialTypeApplication[Cmd]

  @silent final private[algebra] class printPartialTypeApplication[Cmd <: HList](
    private val d: Boolean = true
  ) extends AnyVal {
    def apply[Tgt, Exp](t: Tgt)(
      implicit
      ev1: Valid[Cmd],
      ev2: Last.Aux[Cmd, Exp],
      ev3: Tgt =:= Exp,
      p: Printed[Cmd]
    ): String = s"${p.text} $t"
  }
}
