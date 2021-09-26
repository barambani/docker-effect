package docker.effect
package algebra

import cats.evidence.<~<
import docker.effect.algebra.evidences.{Command, IsInitial}
import eu.timepit.refined.api.Refined
import eu.timepit.refined.generic.Equal
import shapeless.{::, <:!<, HList, Witness}

import scala.annotation.nowarn

sealed trait ValidStart[Cmd <: HList]

object ValidStart {
  @nowarn("msg=parameter value (evidence\\$\\d+|ev\\d+) in method validStart is never used")
  implicit def validStart[I: IsInitial, C: Command, Rem <: HList, LitC, LitS](
    implicit ev1: I <:!< HList,
    ev2: C <:!< HList,
    ev3: I <~< Refined[String, Equal[LitC]],
    ev4: C <~< Refined[String, Equal[LitS]],
    ev5: Witness.Aux[LitC],
    ev6: Witness.Aux[LitS],
    ev7: C CmdCanFollow I
  ): ValidStart[I :: C :: Rem] =
    new ValidStart[I :: C :: Rem] {}
}
