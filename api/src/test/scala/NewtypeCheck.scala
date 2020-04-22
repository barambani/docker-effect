import cats.instances.int._
import cats.instances.list._
import cats.instances.string._
import cats.syntax.eq._
import docker.effect.algebra.SuccessMessage
import docker.effect.internal.newtype
import munit.ScalaCheckSuite
import org.scalacheck.Prop.forAll

final class NewtypePropertyCheck extends ScalaCheckSuite {
  property("unMk gives the original value")(
    forAll { i: Int => newtype[Int](i).unMk === i }
  )

  property("unMkF gives the original values in F[_]")(
    forAll { xs: List[Int] =>
      val nt = newtype[Int]
      nt.unMkF(nt.mkF(xs)) === xs
    }
  )

  test("newtype pattern match") {
    val sm = SuccessMessage("A message")

    sm match {
      case SuccessMessage(m) => m === "A message"
      case other             => fail(s"Expected SuccessMessage(A message) but was `$other`")
    }

    sm match {
      case "A message" => ()
      case other       => fail(s"Expected `A message` but was `$other`")
    }
  }
}

final class NewtypeCheck extends munit.FunSuite {}
