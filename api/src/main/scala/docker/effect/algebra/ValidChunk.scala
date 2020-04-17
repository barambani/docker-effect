package docker.effect
package algebra

import _root_.docker.effect.algebra.evidences._
import cats.evidence.<~<
import com.github.ghik.silencer.silent
import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.And
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.string.MatchesRegex
import shapeless.{ ::, <:!<, HList, HNil, Witness }

sealed trait ValidChunk[Cmd <: HList]

@silent("parameter value (ev.|rec) in method [a-zA-Z0-9]+ is never used")
object ValidChunk extends ValidFinalChunk {
  implicit def validCommand[A, Cmd: Command, Rem <: HList, LitI, LitC](
    implicit
    ev1: A <:!< HList,
    ev2: Cmd <:!< HList,
    ev3: A <~< Refined[String, Equal[LitI]],
    ev4: Cmd <~< Refined[String, Equal[LitC]],
    ev5: Witness.Aux[LitI],
    ev6: Witness.Aux[LitC],
    ev7: Cmd CmdCanFollow A,
    rec: ValidChunk[Cmd :: Rem]
  ): ValidChunk[A :: Cmd :: Rem] = _validChunk[A :: Cmd :: Rem]

  implicit def validVerboseOption[Cmd: Command, VO: VerboseOption, Rem <: HList, LitC, LitO](
    implicit
    ev1: Cmd <:!< HList,
    ev2: VO <:!< HList,
    ev3: Cmd <~< Refined[String, Equal[LitC]],
    ev4: VO <~< Refined[String, Equal[LitO]],
    ev5: Witness.Aux[LitC],
    ev6: Witness.Aux[LitO],
    ev7: Cmd AcceptsVerboseOpt VO,
    rec: ValidChunk[VO :: Rem]
  ): ValidChunk[Cmd :: VO :: Rem] = _validChunk[Cmd :: VO :: Rem]

  implicit def validCompactOption[Cmd: Command, Opt: CompactOption, Rem <: HList, LitC, LitO](
    implicit
    ev1: Cmd <:!< HList,
    ev2: Opt <:!< HList,
    ev3: Cmd <~< Refined[String, Equal[LitC]],
    ev4: Opt <~< Refined[String, Equal[LitO]],
    ev5: Witness.Aux[LitC],
    ev6: Witness.Aux[LitO],
    ev7: Cmd AcceptsCompactOpt Opt,
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
    ev7: Opt AcceptsArgument Arg,
    rec: ValidChunk[Arg :: Rem]
  ): ValidChunk[Opt :: Arg :: Rem] = _validChunk[Opt :: Arg :: Rem]

  implicit def validLastCommandTarget[Prev, Tgt, LitP, LitTgt](
    implicit
    ev1: Prev <:!< HList,
    ev2: Tgt <:!< HList,
    ev3: Prev <~< Refined[String, Equal[LitP]],
    ev4: Tgt <~< Refined[String, NonEmpty And MatchesRegex[LitTgt]],
    ev5: Witness.Aux[LitP],
    ev6: Witness.Aux[LitTgt],
    ev7: Prev AcceptsCmdTarget Tgt
  ): ValidChunk[Prev :: Tgt :: HNil] = _validChunk[Prev :: Tgt :: HNil]

  implicit def validLastCommandTargetPair[Prev, TgtA, TgtB, LitP, LitTgt](
    implicit
    ev1: Prev <:!< HList,
    ev2: TgtA <:!< HList,
    ev3: TgtB <:!< HList,
    ev4: Prev <~< Refined[String, Equal[LitP]],
    ev5: TgtA <~< Refined[String, NonEmpty And MatchesRegex[LitTgt]],
    ev6: TgtB <~< Tag.opaque,
    ev7: Witness.Aux[LitP],
    ev8: Witness.Aux[LitTgt],
    ev9: Prev AcceptsCmdTarget (TgtA, TgtB)
  ): ValidChunk[Prev :: (TgtA, TgtB) :: HNil] = _validChunk[Prev :: (TgtA, TgtB) :: HNil]

  implicit def validLastOptionTarget[Prev, Tgt, LitP, LitTgt](
    implicit
    ev1: Prev <:!< HList,
    ev2: Tgt <:!< HList,
    ev3: Prev <~< Refined[String, Equal[LitP]],
    ev4: Tgt <~< Refined[String, NonEmpty And MatchesRegex[LitTgt]],
    ev5: Witness.Aux[LitP],
    ev6: Witness.Aux[LitTgt],
    ev7: Prev AcceptsOptTarget Tgt
  ): ValidChunk[Prev :: Tgt :: HNil] = _validChunk[Prev :: Tgt :: HNil]

  implicit def validLastOptionTargetPair[Prev, TgtA, TgtB, LitP, LitTgt](
    implicit
    ev1: Prev <:!< HList,
    ev2: TgtA <:!< HList,
    ev3: TgtB <:!< HList,
    ev4: Prev <~< Refined[String, Equal[LitP]],
    ev5: TgtA <~< Refined[String, NonEmpty And MatchesRegex[LitTgt]],
    ev6: TgtB <~< Tag.opaque,
    ev7: Witness.Aux[LitP],
    ev8: Witness.Aux[LitTgt],
    ev9: Prev AcceptsOptTarget (TgtA, TgtB)
  ): ValidChunk[Prev :: (TgtA, TgtB) :: HNil] = _validChunk[Prev :: (TgtA, TgtB) :: HNil]
}

@silent("parameter value (ev.|rec) in method [a-zA-Z0-9]+ is never used")
sealed private[algebra] trait ValidFinalChunk {
  implicit def validLast[SecLst, Lst, LitSecLst, LitLst](
    implicit
    ev1: SecLst <:!< HList,
    ev2: Lst <:!< HList,
    ev3: SecLst <~< Refined[String, Equal[LitSecLst]],
    ev4: Lst <~< Refined[String, Equal[LitLst]],
    ev5: Witness.Aux[LitSecLst],
    ev6: Witness.Aux[LitLst],
    ev7: IsFinal[SecLst, Lst]
  ): ValidChunk[SecLst :: Lst :: HNil] = _validChunk[SecLst :: Lst :: HNil]

  implicit def validLastT2[SecLst, Lst, LitSecLst, LitLstA, LitLstB](
    implicit
    ev1: SecLst <:!< HList,
    ev2: Lst <:!< HList,
    ev3: SecLst <~< Refined[String, Equal[LitSecLst]],
    ev4: Lst <~< (
      Refined[String, Equal[LitLstA]],
      Refined[String, Equal[LitLstB]]
    ),
    ev5: Witness.Aux[LitSecLst],
    ev6: Witness.Aux[LitLstA],
    ev7: Witness.Aux[LitLstB],
    ev9: IsFinal[SecLst, Lst]
  ): ValidChunk[SecLst :: Lst :: HNil] = _validChunk[SecLst :: Lst :: HNil]

  implicit def validLastT3[SecLst, Lst, LitSecLst, LitLstA, LitLstB, LitLstC](
    implicit
    ev1: SecLst <:!< HList,
    ev2: Lst <:!< HList,
    ev3: SecLst <~< Refined[String, Equal[LitSecLst]],
    ev4: Lst <~< (
      Refined[String, Equal[LitLstA]],
      Refined[String, Equal[LitLstB]],
      Refined[String, Equal[LitLstC]]
    ),
    ev5: Witness.Aux[LitSecLst],
    ev6: Witness.Aux[LitLstA],
    ev7: Witness.Aux[LitLstB],
    ev8: Witness.Aux[LitLstC],
    ev9: IsFinal[SecLst, Lst]
  ): ValidChunk[SecLst :: Lst :: HNil] = _validChunk[SecLst :: Lst :: HNil]

  final private[algebra] def _validChunk[A <: HList]: ValidChunk[A] = new ValidChunk[A] {}
}
