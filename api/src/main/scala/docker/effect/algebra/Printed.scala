package docker.effect
package algebra

import cats.evidence.<~<
import com.github.ghik.silencer.silent
import docker.effect.syntax.nes._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.types.string.NonEmptyString
import shapeless.{ ::, <:!<, HList, HNil, Witness }

sealed trait Printed[A] {
  def text: NonEmptyString
}

@silent("parameter value ev. in method [a-zA-Z0-9]+ is never used")
object Printed {
  implicit def printedCommand[ParCmd, ChiCmd, Rem <: HList](
    implicit
    ev1: ValidChunk[ParCmd :: ChiCmd :: Rem],
    ev2: ParCmd :-: ChiCmd,
    prP: Printed[ParCmd],
    rem: Printed[ChiCmd :: Rem]
  ): Printed[ParCmd :: ChiCmd :: Rem] =
    new Printed[ParCmd :: ChiCmd :: Rem] {
      val text: NonEmptyString = prP.text space rem.text
    }

  implicit def printedOption[Cmd, Opt, Rem <: HList](
    implicit
    ev1: ValidChunk[Cmd :: Opt :: Rem],
    ev2: Cmd --| Opt,
    prC: Printed[Cmd],
    rem: Printed[Opt :: Rem]
  ): Printed[Cmd :: Opt :: Rem] =
    new Printed[Cmd :: Opt :: Rem] {
      val text: NonEmptyString = prC.text ++ " --" ++ rem.text
    }

  implicit def printedCompactOption[Cmd, Opt, Rem <: HList](
    implicit
    ev1: ValidChunk[Cmd :: Opt :: Rem],
    ev2: Cmd -| Opt,
    prC: Printed[Cmd],
    rem: Printed[Opt :: Rem]
  ): Printed[Cmd :: Opt :: Rem] =
    new Printed[Cmd :: Opt :: Rem] {
      val text: NonEmptyString = prC.text ++ " -" ++ rem.text
    }

  implicit def printedOptionArgument[Opt, Arg, Rem <: HList](
    implicit
    ev1: ValidChunk[Opt :: Arg :: Rem],
    ev2: Opt =| Arg,
    prO: Printed[Opt],
    rem: Printed[Arg :: Rem]
  ): Printed[Opt :: Arg :: Rem] =
    new Printed[Opt :: Arg :: Rem] {
      val text: NonEmptyString = prO.text ++ "=" ++ rem.text
    }

  implicit def printedLastCommandTgt[Prev, Tgt](
    implicit
    ev1: ValidChunk[Prev :: Tgt :: HNil],
    ev2: Prev \\> Tgt,
    prv: Printed[Prev]
  ): Printed[Prev :: Tgt :: HNil] =
    new Printed[Prev :: Tgt :: HNil] {
      val text: NonEmptyString = prv.text
    }

  implicit def printedLastOptionTgt[Prev, Tgt](
    implicit
    ev1: ValidChunk[Prev :: Tgt :: HNil],
    ev2: Prev /\> Tgt,
    prv: Printed[Prev]
  ): Printed[Prev :: Tgt :: HNil] =
    new Printed[Prev :: Tgt :: HNil] {
      val text: NonEmptyString = prv.text
    }

  implicit def printedLast[Lst](
    implicit
    pl: Printed[Lst]
  ): Printed[Lst :: HNil] =
    new Printed[Lst :: HNil] {
      val text: NonEmptyString = pl.text
    }

  implicit def printedLiteral[Lit, RefLit](
    implicit
    ev1: Lit <:!< HList,
    ev2: Lit <~< Refined[String, Equal[RefLit]],
    wit: Witness.Aux[RefLit]
  ): Printed[Lit] =
    new Printed[Lit] {
      val text: NonEmptyString = NonEmptyString.unsafeFrom(wit.value.toString)
    }
}
