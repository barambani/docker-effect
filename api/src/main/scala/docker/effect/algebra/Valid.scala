package docker.effect
package algebra

import shapeless.HList

sealed trait Valid[Cmd <: HList]

object Valid {
  implicit def isValid[Cmd <: HList: ValidStart: ValidChunk]: Valid[Cmd] = new Valid[Cmd] {}
}
