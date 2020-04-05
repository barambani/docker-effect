package docker.effect
package algebra

import cats.evidence.<~<
import com.github.ghik.silencer.silent
import docker.effect.algebra.evidences.{ Command, Initial }
import eu.timepit.refined.api.Refined
import eu.timepit.refined.generic.Equal
import shapeless.{ ::, <:!<, HList, Witness }

sealed trait ValidStart[Cmd <: HList]

@silent("parameter value ev. in method validStart is never used")
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
  ): ValidStart[I :: Cmd :: Rem] =
    new ValidStart[I :: Cmd :: Rem] {}
}
