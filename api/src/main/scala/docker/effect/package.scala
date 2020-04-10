package docker

import cats.data.Kleisli
import cats.effect.IO
import shapeless.HNil

package object effect {
  final type |[A, B] = Either[A, B]

  final type `.` = HNil

  final type CatsRIO[-R, A] = Kleisli[IO, R, A]
  final type CatsIO[A]      = CatsRIO[Any, A]

  final val CatsRIO = Kleisli
}
