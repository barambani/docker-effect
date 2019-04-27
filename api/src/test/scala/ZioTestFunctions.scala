import org.scalatest.{ Assertion, Matchers }
import scalaz.zio
import scalaz.zio.IO

trait ZioTestFunctions extends Matchers with zio.DefaultRuntime {

  final def successAssert[E, A](io: IO[E, A])(assert: A => Assertion): Assertion =
    unsafeRun(io.either)
      .fold(err => fail(s"Expected success but got $err"), assert)

  final def failureAssert[E, A](io: IO[E, A])(assert: E => Assertion): Assertion =
    unsafeRun(io.either)
      .fold(assert, res => fail(s"Expected failure but got $res"))
}
