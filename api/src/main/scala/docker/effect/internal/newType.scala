package docker.effect
package internal

trait newType[R] {
  type Base <: Any
  private[newtype] trait Tag extends Any
  type T <: Base with Tag

  def apply(a: R): T            = a.asInstanceOf[T]
  def mkF[F[_]](fs: F[R]): F[T] = fs.asInstanceOf[F[T]]
}

object newType {

  def apply[A]: newType[A] = new newType[A] {}

  implicit final private[protocol] class NewTypeSyntax[R](private val t: newType[R]#T)
      extends AnyVal {
    def unMk: R = t.asInstanceOf[R]
  }
}
