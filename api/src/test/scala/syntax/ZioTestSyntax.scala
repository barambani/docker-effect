package syntax

import org.scalatest.Assertion
import scalaz.zio.IO

import scala.language.implicitConversions

trait ZioTestSyntax {
  implicit def zioTestSyntax[E, A](io: IO[E, A]): ZioTestOps[E, A] = new ZioTestOps(io)
}

final private[syntax] class ZioTestOps[E, A](private val io: IO[E, A]) extends AnyVal {

  def satisfies(assert: A => Assertion): Assertion =
    ZioTestFunctions().successAssert(io)(assert)

  def failsWith(assert: E => Assertion): Assertion =
    ZioTestFunctions().failureAssert(io)(assert)
}
