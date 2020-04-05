package docker.effect
package algebra

import cats.Show
import cats.evidence.<~<
import com.github.ghik.silencer.silent
import eu.timepit.refined.api.Refined
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.types.string.NonEmptyString
import shapeless.{ ::, <:!<, HList, HNil, Witness }
import cats.syntax.show._

sealed trait Printed[A] {
  def text: NonEmptyString
}

@silent("parameter value ev. in method [a-zA-Z0-9]+ is never used")
object Printed extends PrintedInstances {
  implicit def printedCommand[ParCmd, ChiCmd, Rem <: HList](
    implicit
    ev1: ValidChunk[ParCmd :: ChiCmd :: Rem],
    ev2: ParCmd :-: ChiCmd,
    prP: Printed[ParCmd],
    rem: Printed[ChiCmd :: Rem]
  ): Printed[ParCmd :: ChiCmd :: Rem] =
    new Printed[ParCmd :: ChiCmd :: Rem] {
      val text: NonEmptyString = NonEmptyString.unsafeFrom(s"${prP.show} ${rem.show}")
    }

  implicit def printedOption[Cmd, Opt, Rem <: HList](
    implicit
    ev1: ValidChunk[Cmd :: Opt :: Rem],
    ev2: Cmd --| Opt,
    prC: Printed[Cmd],
    rem: Printed[Opt :: Rem]
  ): Printed[Cmd :: Opt :: Rem] =
    new Printed[Cmd :: Opt :: Rem] {
      val text: NonEmptyString = NonEmptyString.unsafeFrom(s"${prC.show} --${rem.show}")
    }

  implicit def printedCompactOption[Cmd, Opt, Rem <: HList](
    implicit
    ev1: ValidChunk[Cmd :: Opt :: Rem],
    ev2: Cmd -| Opt,
    prC: Printed[Cmd],
    rem: Printed[Opt :: Rem]
  ): Printed[Cmd :: Opt :: Rem] =
    new Printed[Cmd :: Opt :: Rem] {
      val text: NonEmptyString = NonEmptyString.unsafeFrom(s"${prC.show} -${rem.show}")
    }

  implicit def printedOptionArgument[Opt, Arg, Rem <: HList](
    implicit
    ev1: ValidChunk[Opt :: Arg :: Rem],
    ev2: Opt =| Arg,
    prO: Printed[Opt],
    rem: Printed[Arg :: Rem]
  ): Printed[Opt :: Arg :: Rem] =
    new Printed[Opt :: Arg :: Rem] {
      val text: NonEmptyString = NonEmptyString.unsafeFrom(s"${prO.show}=${rem.show}")
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

sealed private[algebra] trait PrintedInstances {
  implicit def printedShow[A]: Show[Printed[A]] =
    new Show[Printed[A]] {
      def show(t: Printed[A]): String = t.text.value
    }
}
