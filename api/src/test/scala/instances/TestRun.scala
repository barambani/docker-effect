package instances

import docker.effect.{ CatsIO, CatsRIO }
import org.scalatest.Assertion
import org.scalatest.matchers.should.Matchers
import zio.RIO

sealed trait TestRun[F[-_, +_]] extends Matchers {
  def successAssert[A](fa: F[Any, A])(assert: A => Assertion): Assertion
  def failureAssert[A](fa: F[Any, A])(assert: Throwable => Assertion): Assertion
}

object TestRun {
  @inline final def apply[F[-_, +_]](implicit ev: TestRun[F]): TestRun[F] = implicitly

  implicit def zioTestRun: TestRun[RIO] =
    new TestRun[RIO] {
      def successAssert[A](fa: RIO[Any, A])(assert: A => Assertion): Assertion =
        zio.Runtime.default
          .unsafeRun(fa.either)
          .fold(err => fail(s"Expected success but got $err"), assert)

      def failureAssert[A](fa: RIO[Any, A])(assert: Throwable => Assertion): Assertion =
        zio.Runtime.default
          .unsafeRun(fa.either)
          .fold(assert, res => fail(s"Expected failure but got $res"))
    }

  implicit def catsTestRun: TestRun[CatsRIO] =
    new TestRun[CatsRIO] {
      def successAssert[A](fa: CatsIO[A])(assert: A => Assertion): Assertion =
        fa.redeem(err => fail(s"Expected success but got $err"), assert).run.unsafeRunSync

      def failureAssert[A](fa: CatsIO[A])(assert: Throwable => Assertion): Assertion =
        fa.redeem(assert, res => fail(s"Expected failure but got $res")).run.unsafeRunSync
    }
}
