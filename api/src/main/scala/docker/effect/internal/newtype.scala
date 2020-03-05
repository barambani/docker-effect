package docker.effect
package internal

import com.github.ghik.silencer.silent

trait newtype[A] {
  type Base <: Any
  private[newtype] trait Tag extends Any
  type opaque <: Base with Tag

  @silent final private[this] type Id[AA] = AA

  @inline final def apply(a: A): opaque =
    a.asInstanceOf[opaque]

  @inline final def unMk(t: opaque): A =
    unMkF[Id](t)

  @inline final def mkF[F[_]](fa: F[A]): F[opaque] =
    fa.asInstanceOf[F[opaque]]

  @inline final def unMkF[F[_]](ft: F[opaque]): F[A] =
    mkF[λ[α => F[α] => F[A]]](identity)(ft)
}

object newtype {
  @inline def apply[A]: newtype[A] = new newtype[A] {}

  implicit final class NewTypeSyntax[A](private val t: newtype[A]#opaque) extends AnyVal {
    @inline def unMk: A = t.asInstanceOf[A]
  }
}
