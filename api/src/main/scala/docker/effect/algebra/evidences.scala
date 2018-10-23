package docker.effect.algebra

import cats.evidence.<~<
import com.github.ghik.silencer.silent
import docker.effect.algebra.algebra._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.string.MatchesRegex
import shapeless.{ ::, <:!<, HList, HNil, Witness }

@silent object evidences {

  /**
    * Command validation
    */
  sealed trait Valid[Cmd <: HList]

  object Valid extends Alternative1ValidCommand {

    implicit def validCommand[ParCmd, ChiCmd, Rem <: HList, RefParCmd, RefChiCmd](
      implicit
      ev1: ParCmd <:!< HList,
      ev2: ChiCmd <:!< HList,
      ev3: ParCmd <~< Refined[String, Equal[RefParCmd]],
      ev4: ChiCmd <~< Refined[String, Equal[RefChiCmd]],
      ev5: ParCmd :-: ChiCmd,
      rec: Valid[ChiCmd :: Rem]
    ): Valid[ParCmd :: ChiCmd :: Rem] = _valid[ParCmd :: ChiCmd :: Rem]

    implicit def validLast[SecLst, Lst, RefSecLst, RefLst](
      implicit
      ev1: SecLst <:!< HList,
      ev2: Lst <:!< HList,
      ev3: SecLst <~< Refined[String, Equal[RefSecLst]],
      ev4: Lst <~< Refined[String, Equal[RefLst]],
      ev5: Witness.Aux[RefSecLst],
      ev6: Witness.Aux[RefLst],
      ev7: CanEndWith[SecLst, Lst]
    ): Valid[SecLst :: Lst :: HNil] = _valid[SecLst :: Lst :: HNil]

    implicit def validLastTgt[Prev, Tgt, RefPrev, RefTgt](
      implicit
      ev1: Prev <:!< HList,
      ev2: Tgt <:!< HList,
      ev3: Prev <~< Refined[String, Equal[RefPrev]],
      ev4: Tgt <~< Refined[String, MatchesRegex[RefTgt]],
      ev5: Prev \\> Tgt
    ): Valid[Prev :: Tgt :: HNil] = _valid[Prev :: Tgt :: HNil]
  }

  sealed private[effect] trait Alternative1ValidCommand extends Alternative2ValidCommand {

    implicit def validOption[Cmd, Opt, Rem <: HList, RefCmd, RefOpt](
      implicit
      ev1: Cmd <:!< HList,
      ev2: Opt <:!< HList,
      ev3: Cmd <~< Refined[String, Equal[RefCmd]],
      ev4: Opt <~< Refined[String, Equal[RefOpt]],
      ev5: Cmd --| Opt,
      rec: Valid[Opt :: Rem]
    ): Valid[Cmd :: Opt :: Rem] = _valid[Cmd :: Opt :: Rem]
  }

  sealed private[effect] trait Alternative2ValidCommand extends Alternative3ValidCommand {

    implicit def validCompactOption[Cmd, Opt, Rem <: HList, RefCmd, RefOpt](
      implicit
      ev1: Cmd <:!< HList,
      ev2: Opt <:!< HList,
      ev3: Cmd <~< Refined[String, Equal[RefCmd]],
      ev4: Opt <~< Refined[String, Equal[RefOpt]],
      ev5: Cmd -| Opt,
      rec: Valid[Opt :: Rem]
    ): Valid[Cmd :: Opt :: Rem] = _valid[Cmd :: Opt :: Rem]
  }

  sealed private[effect] trait Alternative3ValidCommand {

    implicit def validOptionArgument[Opt, Arg, Rem <: HList, RefOpt, RefArg](
      implicit
      ev1: Opt <:!< HList,
      ev2: Arg <:!< HList,
      ev3: Opt <~< Refined[String, Equal[RefOpt]],
      ev4: Arg <~< Refined[String, Equal[RefArg]],
      ev5: Opt =| Arg,
      rec: Valid[Arg :: Rem]
    ): Valid[Opt :: Arg :: Rem] = _valid[Opt :: Arg :: Rem]

    protected def _valid[A <: HList]: Valid[A] = new Valid[A] {}
  }

  /**
    * Command printing
    */
  sealed trait Printed[A] {
    def text: String
  }

  object Printed extends Alternative1Printed {

    implicit def printedCommand[ParCmd, ChiCmd, Rem <: HList](
      implicit
      ev1: Valid[ParCmd :: ChiCmd :: Rem],
      ev2: ParCmd :-: ChiCmd,
      prP: Printed[ParCmd],
      rem: Printed[ChiCmd :: Rem]
    ): Printed[ParCmd :: ChiCmd :: Rem] =
      new Printed[ParCmd :: ChiCmd :: Rem] {
        val text: String = s"${prP.text} ${rem.text}"
      }
  }

  sealed private[effect] trait Alternative1Printed extends Alternative2Printed {

    implicit def printedOption[Cmd, Opt, Rem <: HList](
      implicit
      ev1: Valid[Cmd :: Opt :: Rem],
      ev2: Cmd --| Opt,
      prC: Printed[Cmd],
      rem: Printed[Opt :: Rem]
    ): Printed[Cmd :: Opt :: Rem] =
      new Printed[Cmd :: Opt :: Rem] {
        val text: String = s"${prC.text} --${rem.text}"
      }
  }

  sealed private[effect] trait Alternative2Printed extends Alternative3Printed {

    implicit def printedCompactOption[Cmd, Opt, Rem <: HList](
      implicit
      ev1: Valid[Cmd :: Opt :: Rem],
      ev2: Cmd -| Opt,
      prC: Printed[Cmd],
      rem: Printed[Opt :: Rem]
    ): Printed[Cmd :: Opt :: Rem] =
      new Printed[Cmd :: Opt :: Rem] {
        val text: String = s"${prC.text} -${rem.text}"
      }
  }

  sealed private[effect] trait Alternative3Printed extends Alternative4Printed {

    implicit def printedOptionArgument[Opt, Arg, Rem <: HList](
      implicit
      ev1: Valid[Opt :: Arg :: Rem],
      ev2: Opt =| Arg,
      prO: Printed[Opt],
      rem: Printed[Arg :: Rem]
    ): Printed[Opt :: Arg :: Rem] =
      new Printed[Opt :: Arg :: Rem] {
        val text: String = s"${prO.text}=${rem.text}"
      }
  }

  sealed private[effect] trait Alternative4Printed {

    implicit def printedLastTgt[Prev, Tgt](
      implicit
      ev1: Valid[Prev :: Tgt :: HNil],
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
