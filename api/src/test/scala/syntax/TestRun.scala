package syntax

import cats.effect.IO
import org.scalatest.Assertion
import org.scalatest.matchers.should.Matchers
import zio.Task

sealed private[syntax] trait TestRun[F[+_]] extends Matchers {
  def successAssert[A](fa: F[A])(assert: A => Assertion): Assertion
  def failureAssert[A](fa: F[A])(assert: Throwable => Assertion): Assertion
}

private[syntax] object TestRun {
  def apply[F[+_]](implicit ev: TestRun[F]): TestRun[F] = implicitly

  implicit def zioTestRun: TestRun[Task] =
    new TestRun[Task] {
      def successAssert[A](fa: Task[A])(assert: A => Assertion): Assertion =
        zio.Runtime.default
          .unsafeRun(fa.either)
          .fold(err => fail(s"Expected success but got $err"), assert)

      def failureAssert[A](fa: Task[A])(assert: Throwable => Assertion): Assertion =
        zio.Runtime.default
          .unsafeRun(fa.either)
          .fold(assert, res => fail(s"Expected failure but got $res"))
    }

  implicit def catsTestRun: TestRun[IO] =
    new TestRun[IO] {
      def successAssert[A](fa: IO[A])(assert: A => Assertion): Assertion =
        fa.attempt.unsafeRunSync.fold(err => fail(s"Expected success but got $err"), assert)

      def failureAssert[A](fa: IO[A])(assert: Throwable => Assertion): Assertion =
        fa.attempt.unsafeRunSync.fold(assert, res => fail(s"Expected failure but got $res"))
    }
}
