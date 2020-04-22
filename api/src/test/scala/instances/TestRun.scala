package instances

import cats.effect.IO
import munit.Assertions
import zio.Task

sealed trait TestRun[F[_]] extends Assertions {
  def unsafe[A](fa: F[A]): A
  def successAssert[A](fa: F[A])(assert: A => Unit): Unit
  def failureAssert[A](fa: F[A])(assert: Throwable => Unit): Unit
}

object TestRun {
  @inline final def apply[F[_]](implicit ev: TestRun[F]): TestRun[F] = ev

  @inline final def unsafe[F[_], A](fa: F[A])(implicit F: TestRun[F]): A = F.unsafe(fa)

  implicit def zioTestRun: TestRun[Task] =
    new TestRun[Task] {
      def successAssert[A](fa: Task[A])(assert: A => Unit): Unit =
        zio.Runtime.default
          .unsafeRun(fa.either)
          .fold(err => fail(s"Expected success but got $err"), assert)

      def failureAssert[A](fa: Task[A])(assert: Throwable => Unit): Unit =
        zio.Runtime.default
          .unsafeRun(fa.either)
          .fold(assert, res => fail(s"Expected failure but got $res"))

      def unsafe[A](fa: Task[A]): A =
        zio.Runtime.default.unsafeRun(fa)
    }

  implicit def catsTestRun: TestRun[IO] =
    new TestRun[IO] {
      def successAssert[A](fa: IO[A])(assert: A => Unit): Unit =
        fa.redeem(err => fail(s"Expected success but got $err"), assert).unsafeRunSync

      def failureAssert[A](fa: IO[A])(assert: Throwable => Unit): Unit =
        fa.redeem(assert, res => fail(s"Expected failure but got $res")).unsafeRunSync

      def unsafe[A](fa: IO[A]): A = fa.unsafeRunSync()
    }
}
