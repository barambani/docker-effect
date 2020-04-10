package docker

import cats.effect.IO
import docker.effect.interop.{ CovariantKleisli, CovariantKleisliAny }
import shapeless.HNil

package object effect {
  final type |[A, B] = Either[A, B]

  final type `.` = HNil

  final type CatsRIO[-R, +A] = CovariantKleisli[IO, R, A]
  final type CatsIO[+A]      = CatsRIO[Any, A]

  final val CatsRIO = CovariantKleisli
  final val CatsIO  = CovariantKleisliAny
}
