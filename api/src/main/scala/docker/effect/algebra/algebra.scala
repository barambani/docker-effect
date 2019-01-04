package docker
package effect
package algebra

import com.github.ghik.silencer.silent
import docker.effect.algebra.evidences._
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

  //  verbose options
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

  //  signals
  final type KILL = String Refined Equal[W.`"KILL"`.T]
  final type HUP  = String Refined Equal[W.`"HUP"`.T]

  //  targets
  final type Id   = String Refined MatchesRegex[W.`"[0-9a-fA-F]+"`.T]
  final type Name = String Refined MatchesRegex[W.`"[-0-9a-zA-Z]+"`.T]
  final type Repo = String Refined MatchesRegex[W.`"[-0-9a-zA-Z]+"`.T]
  final type Tag  = Tag.T

  final object Id   extends RefinedTypeOps[Id, String]
  final object Name extends RefinedTypeOps[Name, String]
  final object Repo extends RefinedTypeOps[Repo, String]
  final val Tag = newtype[NonEmptyString]

  // relationships
  final type :-:[In, Cmd]  = CommandAllowed[In, Cmd]
  final type --|[Cmd, Opt] = VerboseOptionAllowed[Cmd, Opt]
  final type -|[Cmd, Opt]  = CompactOptionAllowed[Cmd, Opt]
  final type =|[Opt, Arg]  = OptionArgumentAllowed[Opt, Arg]
  final type \\>[Prv, Tgt] = CommandTargetAllowed[Prv, Tgt]

  //  printer
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
