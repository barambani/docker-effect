package docker.effect

import docker.effect.algebra.evidences._
import docker.effect.algebra.newtypes.{MkDockerCommand, MkSuccessMessage}
import docker.effect.internal.newtype
import docker.effect.syntax.nes._
import eu.timepit.refined.W
import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import eu.timepit.refined.auto._
import eu.timepit.refined.boolean.And
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.types.string.NonEmptyString
import shapeless.HList
import shapeless.ops.hlist.Last

import scala.annotation.nowarn

package object algebra {

  final val SuccessMessage = MkSuccessMessage
  final type SuccessMessage = SuccessMessage.opaque

  final val DockerCommand = MkDockerCommand
  final type DockerCommand = DockerCommand.opaque

  //  commands
  final type docker    = String Refined Equal[W.`"docker"`.T]
  final type container = String Refined Equal[W.`"container"`.T]
  final type images    = String Refined Equal[W.`"images"`.T]
  final type kill      = String Refined Equal[W.`"kill"`.T]
  final type rm        = String Refined Equal[W.`"rm"`.T]
  final type rmi       = String Refined Equal[W.`"rmi"`.T]
  final type run       = String Refined Equal[W.`"run"`.T]
  final type ps        = String Refined Equal[W.`"ps"`.T]
  final type pull      = String Refined Equal[W.`"pull"`.T]
  final type stop      = String Refined Equal[W.`"stop"`.T]

  //  verbose options
  final type all        = String Refined Equal[W.`"all"`.T]
  final type digest     = String Refined Equal[W.`"digest"`.T]
  final type detach     = String Refined Equal[W.`"detach"`.T]
  final type filter     = String Refined Equal[W.`"filter"`.T]
  final type force      = String Refined Equal[W.`"force"`.T]
  final type format     = String Refined Equal[W.`"format"`.T]
  final type `no-trunc` = String Refined Equal[W.`"no-trunc"`.T]
  final type quiet      = String Refined Equal[W.`"quiet"`.T]
  final type last       = String Refined Equal[W.`"last"`.T]
  final type latest     = String Refined Equal[W.`"latest"`.T]
  final type size       = String Refined Equal[W.`"size"`.T]
  final type signal     = String Refined Equal[W.`"signal"`.T]

  //  compact options
  final type a = String Refined Equal[W.`"a"`.T]
  final type d = String Refined Equal[W.`"d"`.T]
  final type f = String Refined Equal[W.`"f"`.T]
  final type q = String Refined Equal[W.`"q"`.T]
  final type l = String Refined Equal[W.`"l"`.T]
  final type s = String Refined Equal[W.`"s"`.T]

  //  compact composite options
  final type aq = String Refined Equal[W.`"aq"`.T]

  //  signals
  final type KILL = String Refined Equal[W.`"KILL"`.T]
  final type HUP  = String Refined Equal[W.`"HUP"`.T]

  //  targets
  final type IdRef   = NonEmpty And MatchesRegex[W.`"[0-9a-fA-F]+"`.T]
  final type TextRef = NonEmpty And MatchesRegex[W.`"[-0-9a-zA-Z]+"`.T]

  final type TextString = String Refined TextRef
  final object TextString extends RefinedTypeOps[TextString, String]

  final object Id    extends RefinedTypeOps[Id, String]
  final object Image extends newtype[TextString]
  final object Name  extends newtype[TextString]
  final object Repo  extends newtype[TextString]
  final object Tag   extends newtype[NonEmptyString]
  final val latest = Tag("latest")

  final type Id    = String Refined IdRef
  final type Image = Image.opaque
  final type Name  = Name.opaque
  final type Repo  = Repo.opaque
  final type Tag   = Tag.opaque

  // relationships
  final type CmdCanFollow[Cmd, In]       = CommandAllowed[In, Cmd]
  final type AcceptsVerboseOpt[Cmd, Opt] = VerboseOptionAllowed[Cmd, Opt]
  final type AcceptsCompactOpt[Cmd, Opt] = CompactOptionAllowed[Cmd, Opt]
  final type AcceptsArgument[Opt, Arg]   = OptionArgumentAllowed[Opt, Arg]
  final type AcceptsCmdTarget[Cmd, Tgt]  = CommandTargetAllowed[Cmd, Tgt]
  final type AcceptsOptTarget[Opt, Tgt]  = OptionTargetAllowed[Opt, Tgt]

  // printer
  @nowarn("msg=parameter value evidence\\$\\d+ in method printed0 is never used")
  final def printed0[Cmd <: HList: Valid](implicit p: Printed[Cmd]): DockerCommand =
    DockerCommand(p.text)

  final def printed1[Cmd <: HList]: printPartialTypeApplicationTuple[Cmd] =
    new printPartialTypeApplicationTuple[Cmd]

  @nowarn("msg=it is not recommended to define classes/objects inside of package objects.")
  private[algebra] final class printPartialTypeApplicationTuple[Cmd <: HList](
    private val `_`: Boolean = true
  ) extends AnyVal
      with printPartialTypeApplication[Cmd] {
    @nowarn("msg=parameter value ev\\d+ in method apply is never used")
    def apply[Tgt, ExpA, ExpB](t: (ExpA, ExpB))(
      implicit ev1: Valid[Cmd],
      ev2: Last.Aux[Cmd, Tgt],
      ev3: Tgt =:= (ExpA, ExpB),
      p: Printed[Cmd]
    ): DockerCommand =
      DockerCommand(p.text + s" ${t._1.toString}:${t._2.toString}")
  }

  @nowarn("msg=it is not recommended to define classes/objects inside of package objects.")
  private[algebra] sealed trait printPartialTypeApplication[Cmd <: HList] extends Any {
    @nowarn("msg=parameter value ev\\d+ in method apply is never used")
    def apply[Tgt, Exp](t: Tgt)(
      implicit ev1: Valid[Cmd],
      ev2: Last.Aux[Cmd, Exp],
      ev3: Tgt =:= Exp,
      p: Printed[Cmd]
    ): DockerCommand =
      DockerCommand(p.text + " " + t.toString)
  }
}
