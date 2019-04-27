package docker

import cats.data.{ EitherT, ReaderT }
import cats.{ effect => ce }
import shapeless.HNil

package object effect {

  final type |[A, B] = Either[A, B]

  final type `.` = HNil

  final type CatsBio[R, E, A] = EitherT[ReaderT[ce.IO, R, ?], E, A]
}
