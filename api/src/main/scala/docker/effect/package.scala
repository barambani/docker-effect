package docker

import shapeless.HNil

package object effect {

  final type |[A, B] = Either[A, B]

  final type `.` = HNil
}
