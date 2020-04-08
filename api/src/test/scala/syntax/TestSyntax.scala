package syntax

import org.scalatest.Assertion

import scala.language.implicitConversions

trait TestSyntax {
  implicit def testSyntax[F[+_], A](fa: F[A]): TestOps[F, A] = new TestOps(fa)
}

final private[syntax] class TestOps[F[+_], A](private val fa: F[A]) extends AnyVal {
  def satisfies(assert: A => Assertion)(implicit F: TestRun[F]): Assertion =
    F.successAssert(fa)(assert)

  def failsWith(assert: Throwable => Assertion)(implicit F: TestRun[F]): Assertion =
    F.failureAssert(fa)(assert)
}
