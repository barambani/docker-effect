package docker
package effect
package internal

import com.github.ghik.silencer.silent

trait newtype[A] {
  type Base <: Any
  private[newtype] trait Tag extends Any
  type T <: Base with Tag

  @silent final private[this] type Id[AA] = AA

  @inline final def apply(a: A): T =
    a.asInstanceOf[T]

  @inline final def unMk(t: T): A =
    unMkF[Id](t)

  @inline final def mkF[F[_]](fa: F[A]): F[T] =
    fa.asInstanceOf[F[T]]

  @inline final def unMkF[F[_]](ft: F[T]): F[A] =
    mkF[λ[α => F[α] => F[A]]](identity)(ft)
}

object newtype {

  @inline def apply[A]: newtype[A] = new newtype[A] {}

  implicit final class NewTypeSyntax[A](private val t: newtype[A]#T) extends AnyVal {
    @inline def unMk: A = t.asInstanceOf[A]
  }
}
