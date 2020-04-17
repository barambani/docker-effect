package docker.effect
package interop

trait RioMonadError[F[-_, _]] extends RioMonad[F] {
  def failed[R, A](e: Throwable): F[R, A]
  def redeemWith[R, A, B](fa: F[R, A])(failure: Throwable => F[R, B], success: A => F[R, B]): F[R, B]
}

object RioMonadError {
  @inline final def apply[F[-_, _]](implicit ev: RioMonadError[F]): RioMonadError[F] = implicitly

  @inline def absolve[F[-_, _], R, A](fa: F[R, Either[String, A]])(
    implicit me: RioMonadError[F]
  ): F[R, A] =
    me.>>=(fa)(
      _.fold(
        s => me.failed(new Exception(s)),
        me.pure[R, A]
      )
    )
}
