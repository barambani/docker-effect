package docker.effect
package algebra

import shapeless.HList

import scala.annotation.nowarn

sealed trait Valid[Cmd <: HList]

object Valid {
  @nowarn("msg=parameter value evidence\\$\\d+ in method isValid is never used")
  implicit def isValid[Cmd <: HList: ValidStart: ValidChunk]: Valid[Cmd] = new Valid[Cmd] {}
}
