package docker
package effect
package algebra

import _root_.docker.effect.algebra.evidences._
import cats.evidence.<~<
import com.github.ghik.silencer.silent
import docker.effect.algebra.algebra._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.string.MatchesRegex
import shapeless.{ ::, <:!<, HList, HNil, Witness }

@silent object proofs {

  /**
    * Command validation
    */
  sealed trait Valid[Cmd <: HList]

  object Valid {
    implicit def isValid[Cmd <: HList: ValidStart: ValidChunk]: Valid[Cmd] = new Valid[Cmd] {}
  }

  sealed trait ValidStart[Cmd <: HList]

  object ValidStart {

    implicit def validStart[I: Initial, Cmd: Command, Rem <: HList, LitC, LitS](
      implicit
      ev1: I <:!< HList,
      ev2: Cmd <:!< HList,
      ev3: I <~< Refined[String, Equal[LitC]],
      ev4: Cmd <~< Refined[String, Equal[LitS]],
      ev5: Witness.Aux[LitC],
      ev6: Witness.Aux[LitS],
      ev7: I :-: Cmd
    ): ValidStart[I :: Cmd :: Rem] = new ValidStart[I :: Cmd :: Rem] {}
  }

  sealed trait ValidChunk[Cmd <: HList]

  object ValidChunk {

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

    implicit def validLastTgt[Prev, Tgt, LitP, LitTgt](
      implicit
      ev1: Prev <:!< HList,
      ev2: Tgt <:!< HList,
      ev3: Prev <~< Refined[String, Equal[LitP]],
      ev4: Tgt <~< Refined[String, MatchesRegex[LitTgt]],
      ev5: Witness.Aux[LitP],
      ev6: Witness.Aux[LitTgt],
      ev7: Prev \\> Tgt
    ): ValidChunk[Prev :: Tgt :: HNil] = _validChunk[Prev :: Tgt :: HNil]

    final private[this] def _validChunk[A <: HList]: ValidChunk[A] = new ValidChunk[A] {}
  }

  /**
    * Command printing
    */
  sealed trait Printed[A] {
    def text: String
  }

  object Printed {

    implicit def printedCommand[ParCmd, ChiCmd, Rem <: HList](
      implicit
      ev1: ValidChunk[ParCmd :: ChiCmd :: Rem],
      ev2: ParCmd :-: ChiCmd,
      prP: Printed[ParCmd],
      rem: Printed[ChiCmd :: Rem]
    ): Printed[ParCmd :: ChiCmd :: Rem] =
      new Printed[ParCmd :: ChiCmd :: Rem] {
        val text: String = s"${prP.text} ${rem.text}"
      }

    implicit def printedOption[Cmd, Opt, Rem <: HList](
      implicit
      ev1: ValidChunk[Cmd :: Opt :: Rem],
      ev2: Cmd --| Opt,
      prC: Printed[Cmd],
      rem: Printed[Opt :: Rem]
    ): Printed[Cmd :: Opt :: Rem] =
      new Printed[Cmd :: Opt :: Rem] {
        val text: String = s"${prC.text} --${rem.text}"
      }

    implicit def printedCompactOption[Cmd, Opt, Rem <: HList](
      implicit
      ev1: ValidChunk[Cmd :: Opt :: Rem],
      ev2: Cmd -| Opt,
      prC: Printed[Cmd],
      rem: Printed[Opt :: Rem]
    ): Printed[Cmd :: Opt :: Rem] =
      new Printed[Cmd :: Opt :: Rem] {
        val text: String = s"${prC.text} -${rem.text}"
      }

    implicit def printedOptionArgument[Opt, Arg, Rem <: HList](
      implicit
      ev1: ValidChunk[Opt :: Arg :: Rem],
      ev2: Opt =| Arg,
      prO: Printed[Opt],
      rem: Printed[Arg :: Rem]
    ): Printed[Opt :: Arg :: Rem] =
      new Printed[Opt :: Arg :: Rem] {
        val text: String = s"${prO.text}=${rem.text}"
      }

    implicit def printedLastTgt[Prev, Tgt](
      implicit
      ev1: ValidChunk[Prev :: Tgt :: HNil],
      ev2: Prev \\> Tgt,
      prv: Printed[Prev]
    ): Printed[Prev :: Tgt :: HNil] =
      new Printed[Prev :: Tgt :: HNil] {
        val text: String = prv.text
      }

    implicit def printedLast[Lst](
      implicit
      pl: Printed[Lst]
    ): Printed[Lst :: HNil] =
      new Printed[Lst :: HNil] {
        val text: String = pl.text
      }

    implicit def printedLiteral[Lit, RefLit](
      implicit
      ev1: Lit <:!< HList,
      ev2: Lit <~< Refined[String, Equal[RefLit]],
      wit: Witness.Aux[RefLit]
    ): Printed[Lit] =
      new Printed[Lit] {
        val text: String = wit.value.toString
      }
  }
}
