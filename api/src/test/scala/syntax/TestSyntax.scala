package syntax

import instances.TestRun

import scala.language.implicitConversions

trait TestSyntax {
  implicit def testSyntax[F[_], A](fa: F[A]): TestOps[F, A] = new TestOps(fa)
}

final private[syntax] class TestOps[F[_], A](private val fa: F[A]) extends AnyVal {
  def satisfies(assert: A => Unit)(implicit F: TestRun[F]): Unit =
    F.successAssert(fa)(assert)

  def failsWith(assert: Throwable => Unit)(implicit F: TestRun[F]): Unit =
    F.failureAssert(fa)(assert)
}
