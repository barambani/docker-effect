package syntax

import org.scalatest.Assertion
import org.scalatest.matchers.should.Matchers
import zio.IO

sealed abstract private[syntax] class ZioTestFunctions extends Matchers {
  final def successAssert[E, A](io: IO[E, A])(assert: A => Assertion): Assertion =
    zio.Runtime.default
      .unsafeRun(io.either)
      .fold(err => fail(s"Expected success but got $err"), assert)

  final def failureAssert[E, A](io: IO[E, A])(assert: E => Assertion): Assertion =
    zio.Runtime.default
      .unsafeRun(io.either)
      .fold(assert, res => fail(s"Expected failure but got $res"))
}

private[syntax] object ZioTestFunctions {
  def apply(): ZioTestFunctions = new ZioTestFunctions {}
}
