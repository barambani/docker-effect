package docker
package effect
package algebra

import _root_.docker.effect.algebra.evidences._
import cats.evidence.<~<
import com.github.ghik.silencer.silent
import eu.timepit.refined.api.Refined
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.string.MatchesRegex
import shapeless.{ ::, <:!<, HList, HNil, Witness }

sealed trait ValidChunk[Cmd <: HList]

@silent object ValidChunk {

  implicit def validCommand[A, Cmd: Command, Rem <: HList, LitI, LitC](
    implicit
    ev1: A <:!< HList,
    ev2: Cmd <:!< HList,
    ev3: A <~< Refined[String, Equal[LitI]],
    ev4: Cmd <~< Refined[String, Equal[LitC]],
    ev5: Witness.Aux[LitI],
    ev6: Witness.Aux[LitC],
    ev7: A :-: Cmd,
    rec: ValidChunk[Cmd :: Rem]
  ): ValidChunk[A :: Cmd :: Rem] = _validChunk[A :: Cmd :: Rem]

  implicit def validVerboseOption[Cmd: Command, Opt: VerboseOption, Rem <: HList, LitC, LitO](
    implicit
    ev1: Cmd <:!< HList,
    ev2: Opt <:!< HList,
    ev3: Cmd <~< Refined[String, Equal[LitC]],
    ev4: Opt <~< Refined[String, Equal[LitO]],
    ev5: Witness.Aux[LitC],
    ev6: Witness.Aux[LitO],
    ev7: Cmd --| Opt,
    rec: ValidChunk[Opt :: Rem]
  ): ValidChunk[Cmd :: Opt :: Rem] = _validChunk[Cmd :: Opt :: Rem]

  implicit def validCompactOption[Cmd: Command, Opt: CompactOption, Rem <: HList, LitC, LitO](
    implicit
    ev1: Cmd <:!< HList,
    ev2: Opt <:!< HList,
    ev3: Cmd <~< Refined[String, Equal[LitC]],
    ev4: Opt <~< Refined[String, Equal[LitO]],
    ev5: Witness.Aux[LitC],
    ev6: Witness.Aux[LitO],
    ev7: Cmd -| Opt,
    rec: ValidChunk[Opt :: Rem]
  ): ValidChunk[Cmd :: Opt :: Rem] = _validChunk[Cmd :: Opt :: Rem]

  implicit def validOptionArgument[Opt, Arg, Rem <: HList, LitO, LitA](
    implicit
    ev1: Opt <:!< HList,
    ev2: Arg <:!< HList,
    ev3: Opt <~< Refined[String, Equal[LitO]],
    ev4: Arg <~< Refined[String, Equal[LitA]],
    ev5: Witness.Aux[LitO],
    ev6: Witness.Aux[LitA],
    ev7: Opt =| Arg,
    rec: ValidChunk[Arg :: Rem]
  ): ValidChunk[Opt :: Arg :: Rem] = _validChunk[Opt :: Arg :: Rem]

  implicit def validLast[SecLst, Lst, LitSecLst, LitLst](
    implicit
    ev1: SecLst <:!< HList,
    ev2: Lst <:!< HList,
    ev3: SecLst <~< Refined[String, Equal[LitSecLst]],
    ev4: Lst <~< Refined[String, Equal[LitLst]],
    ev5: Witness.Aux[LitSecLst],
    ev6: Witness.Aux[LitLst],
    ev7: CanEndWith[SecLst, Lst]
  ): ValidChunk[SecLst :: Lst :: HNil] = _validChunk[SecLst :: Lst :: HNil]

  implicit def validLastCommandTgt[Prev, Tgt, LitP, LitTgt](
    implicit
    ev1: Prev <:!< HList,
    ev2: Tgt <:!< HList,
    ev3: Prev <~< Refined[String, Equal[LitP]],
    ev4: Tgt <~< Refined[String, MatchesRegex[LitTgt]],
    ev5: Witness.Aux[LitP],
    ev6: Witness.Aux[LitTgt],
    ev7: Prev \\> Tgt
  ): ValidChunk[Prev :: Tgt :: HNil] = _validChunk[Prev :: Tgt :: HNil]

  implicit def validLastOptionTgt[Prev, Tgt, LitP, LitTgt](
    implicit
    ev1: Prev <:!< HList,
    ev2: Tgt <:!< HList,
    ev3: Prev <~< Refined[String, Equal[LitP]],
    ev4: Tgt <~< Refined[String, MatchesRegex[LitTgt]],
    ev5: Witness.Aux[LitP],
    ev6: Witness.Aux[LitTgt],
    ev7: Prev /\> Tgt
  ): ValidChunk[Prev :: Tgt :: HNil] = _validChunk[Prev :: Tgt :: HNil]

  final private[this] def _validChunk[A <: HList]: ValidChunk[A] = new ValidChunk[A] {}
}
