package docker

import cats.data.EitherT
import cats.{ effect => ce }

package object effect {

  final type |[A, B]       = Either[A, B]
  final type CatsBio[E, X] = EitherT[ce.IO, E, X]
}
