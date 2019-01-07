import docker.effect.CatsBio
import org.scalatest.{ Assertion, Matchers }

trait CatsBioTestFunctions extends Matchers {

  final def successAssert[E, A](io: CatsBio[E, A])(assert: A => Assertion): Assertion =
    io.fold(
        err => fail(s"Expected success but got $err"),
        assert
      )
      .unsafeRunSync()

  final def failureAssert[E, A](io: CatsBio[E, A])(assert: E => Assertion): Assertion =
    io.fold(
        assert,
        res => fail(s"Expected failure but got $res")
      )
      .unsafeRunSync()
}
