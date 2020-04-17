import cats.instances.int._
import cats.instances.list._
import cats.syntax.eq._
import docker.effect.algebra.SuccessMessage
import docker.effect.internal.newtype
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class NewtypePropertyCheck extends Properties("newtype") {
  property("unMk gives the original value") = forAll { i: Int => newtype[Int](i).unMk === i }

  property("unMkF gives the original values in F[_]") = forAll { xs: List[Int] =>
    val nt = newtype[Int]
    nt.unMkF(nt.mkF(xs)) === xs
  }
}

final class NewtypeCheck extends AnyWordSpecLike with Matchers {
  "Can be used in pattern match" in {
    val sm = SuccessMessage("A message")

    sm match {
      case SuccessMessage(m) => m shouldBe "A message"
      case other             => fail(s"Expected SuccessMessage(A message) but was `$other`")
    }

    sm match {
      case "A message" => succeed
      case other       => fail(s"Expected `A message` but was `$other`")
    }
  }
}
