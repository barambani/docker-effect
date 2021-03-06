package docker.effect
package internal

trait newtype[A] {
  type Base <: Any
  private[newtype] trait Tag extends Any
  type opaque <: Base with Tag

  private[this] final type Id[AA] = AA

  @inline final def apply(a: A): opaque =
    a.asInstanceOf[opaque]

  @inline final def unMk(t: opaque): A =
    unMkF[Id](t)

  @inline final def mkF[F[_]](fa: F[A]): F[opaque] =
    fa.asInstanceOf[F[opaque]]

  @inline final def unMkF[F[_]](ft: F[opaque]): F[A] =
    mkF[λ[α => F[α] => F[A]]](identity)(ft)

  @inline final def unapply(t: opaque): Option[A] = Some(unMk(t))
}

object newtype {
  @inline def apply[A]: newtype[A] = new newtype[A] {}

  implicit final class NewTypeSyntax[A](private val t: newtype[A]#opaque) extends AnyVal {
    @inline def unMk: A = t.asInstanceOf[A]
  }
}
