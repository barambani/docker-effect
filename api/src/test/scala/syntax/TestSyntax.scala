package syntax

import instances.TestRun
import org.scalatest.Assertion

import scala.language.implicitConversions

trait TestSyntax {
  implicit def testSyntax[F[-_, +_], A](fa: F[Any, A]): TestOps[F, A] = new TestOps(fa)
}

final private[syntax] class TestOps[F[-_, +_], A](private val fa: F[Any, A]) extends AnyVal {
  def satisfies(assert: A => Assertion)(implicit F: TestRun[F]): Assertion =
    F.successAssert(fa)(assert)

  def failsWith(assert: Throwable => Assertion)(implicit F: TestRun[F]): Assertion =
    F.failureAssert(fa)(assert)
}
