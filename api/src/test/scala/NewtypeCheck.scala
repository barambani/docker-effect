import cats.instances.int._
import cats.instances.list._
import cats.syntax.eq._
import docker.effect.internal.newtype
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

final class NewtypeCheck extends Properties("newtype") {
  property("unMk gives the original value") = forAll { i: Int =>
    newtype[Int](i).unMk === i
  }

  property("unMkF gives the original values in F[_]") = forAll { xs: List[Int] =>
    val nt = newtype[Int]
    nt.unMkF(nt.mkF(xs)) === xs
  }
}
