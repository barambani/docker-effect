package docker.effect
package algebra

import cats.evidence.<~<
import docker.effect.syntax.nes._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.types.string.NonEmptyString
import shapeless.{::, <:!<, HList, HNil, Witness}

import scala.annotation.nowarn

sealed trait Printed[A] {
  def text: NonEmptyString
}

@nowarn("msg=parameter value ev\\d+ in method \\w+ is never used")
object Printed {
  implicit def printedCommand[Par, Chi, Rem <: HList](
    implicit ev1: ValidChunk[Par :: Chi :: Rem],
    ev2: Chi CmdCanFollow Par,
    prP: Printed[Par],
    rem: Printed[Chi :: Rem]
  ): Printed[Par :: Chi :: Rem] =
    new Printed[Par :: Chi :: Rem] {
      val text: NonEmptyString = prP.text space rem.text
    }

  implicit def printedOption[Cmd, Opt, Rem <: HList](
    implicit ev1: ValidChunk[Cmd :: Opt :: Rem],
    ev2: Cmd AcceptsVerboseOpt Opt,
    prC: Printed[Cmd],
    rem: Printed[Opt :: Rem]
  ): Printed[Cmd :: Opt :: Rem] =
    new Printed[Cmd :: Opt :: Rem] {
      val text: NonEmptyString = prC.text ++ " --" ++ rem.text
    }

  implicit def printedOptionT3[Cmd, OptA, OptB, OptC, Rem <: HList](
    implicit ev1: ValidChunk[Cmd :: (OptA, OptB, OptC) :: Rem],
    ev2: Cmd AcceptsVerboseOpt (OptA, OptB, OptC),
    pCm: Printed[Cmd],
    prA: Printed[OptA],
    prB: Printed[OptB],
    rem: Printed[OptC :: Rem]
  ): Printed[Cmd :: (OptA, OptB, OptC) :: Rem] =
    new Printed[Cmd :: (OptA, OptB, OptC) :: Rem] {
      val text: NonEmptyString =
        pCm.text ++ " --" ++ prA.text ++ " --" ++ prB.text ++ " --" ++ rem.text
    }

  implicit def printedCompactOption[Cmd, Opt, Rem <: HList](
    implicit ev1: ValidChunk[Cmd :: Opt :: Rem],
    ev2: Cmd AcceptsCompactOpt Opt,
    prC: Printed[Cmd],
    rem: Printed[Opt :: Rem]
  ): Printed[Cmd :: Opt :: Rem] =
    new Printed[Cmd :: Opt :: Rem] {
      val text: NonEmptyString = prC.text ++ " -" ++ rem.text
    }

  implicit def printedOptionArgument[Opt, Arg, Rem <: HList](
    implicit ev1: ValidChunk[Opt :: Arg :: Rem],
    ev2: Opt AcceptsArgument Arg,
    prO: Printed[Opt],
    rem: Printed[Arg :: Rem]
  ): Printed[Opt :: Arg :: Rem] =
    new Printed[Opt :: Arg :: Rem] {
      val text: NonEmptyString = prO.text ++ "=" ++ rem.text
    }

  implicit def printedLastCommandTgt[Prev, Tgt](
    implicit ev1: ValidChunk[Prev :: Tgt :: HNil],
    ev2: Prev AcceptsCmdTarget Tgt,
    prv: Printed[Prev]
  ): Printed[Prev :: Tgt :: HNil] =
    new Printed[Prev :: Tgt :: HNil] {
      val text: NonEmptyString = prv.text
    }

  implicit def printedLastOptionTgt[Prev, Tgt](
    implicit ev1: ValidChunk[Prev :: Tgt :: HNil],
    ev2: Prev AcceptsOptTarget Tgt,
    prv: Printed[Prev]
  ): Printed[Prev :: Tgt :: HNil] =
    new Printed[Prev :: Tgt :: HNil] {
      val text: NonEmptyString = prv.text
    }

  implicit def printedLast[Lst](
    implicit pl: Printed[Lst]
  ): Printed[Lst :: HNil] =
    new Printed[Lst :: HNil] {
      val text: NonEmptyString = pl.text
    }

  implicit def printedLiteral[Lit, RefLit](
    implicit ev1: Lit <:!< HList,
    ev2: Lit <~< Refined[String, Equal[RefLit]],
    wit: Witness.Aux[RefLit]
  ): Printed[Lit] =
    new Printed[Lit] {
      val text: NonEmptyString = NonEmptyString.unsafeFrom(wit.value.toString)
    }
}
