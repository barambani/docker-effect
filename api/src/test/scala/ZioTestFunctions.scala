import org.scalatest.{ Assertion, Matchers }
import scalaz.zio.{ ExitResult, IO, RTS }

trait ZioTestFunctions extends Matchers {

  final object TestRunner extends RTS

  final def successAssert[E, A](io: IO[E, A])(assert: A => Assertion): ExitResult[Nothing, Assertion] =
    TestRunner
      .unsafeRunSync(io)
      .bimap(err => fail(s"Expected success but got $err"), assert)

  final def failureAssert[E, A](io: IO[E, A])(assert: E => Assertion): ExitResult[Assertion, Nothing] =
    TestRunner
      .unsafeRunSync(io)
      .bimap(assert, res => fail(s"Expected failure but got $res"))
}
