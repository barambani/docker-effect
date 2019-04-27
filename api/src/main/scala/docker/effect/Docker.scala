package docker
package effect

import _root_.docker.effect.Docker.runPartialTypeApplication
import _root_.docker.effect.algebra._
import com.github.ghik.silencer.silent
import scalaz.zio.ZIO
import shapeless.ops.hlist.Last
import shapeless.{ ::, HList }

sealed trait aaaaaa[F[- _, + _, + _]] {
  def bbbbbb[R, E, A](f: R => F[R, E, A]): F[R, E, A]
}
object aaaaaa {

  implicit val zioaaaaaa: aaaaaa[ZIO] =
    new aaaaaa[ZIO] {
      def bbbbbb[R, E, A](f: R => ZIO[R, E, A]): ZIO[R, E, A] = ZIO.accessM(f)
    }
}

sealed trait cccccc[F[- _, + _, + _]] {
  def provided[R, E, A](fa: F[R, E, A])(r: =>R): F[Any, E, A]
}
object cccccc {

  implicit val ziocccccc: cccccc[ZIO] =
    new cccccc[ZIO] {
      def provided[R, E, A](fa: ZIO[R, E, A])(r: =>R): ZIO[Any, E, A] = fa.provide(r)
    }
}

@silent abstract class Docker[F[- _, + _, + _]](
  implicit access: aaaaaa[F],
  cccc: cccccc[F],
  command: Command[F]
) {

  val runContainer: F[Name | Id, ErrorMessage, SuccessMessage] =
    access.bbbbbb {
      _.fold(
        run1[docker :: run :: Name :: `.`](_),
        run1[docker :: run :: Id :: `.`](_)
      )
    }

  val stopContainer: F[Name | Id, ErrorMessage, SuccessMessage] = ???
//    access.bbbbbb {
//      _.fold(
//        run1[docker :: stop :: Name :: `.`](_),
//        run1[docker :: stop :: Id :: `.`](_)
//      )
//    }

  val killContainer: F[Name | Id, ErrorMessage, SuccessMessage]        = ???
  val removeContainer: F[Name | Id, ErrorMessage, SuccessMessage]      = ???
  val forceRemoveContainer: F[Name | Id, ErrorMessage, SuccessMessage] = ???
  val removeAllContainers: F[Any, ErrorMessage, SuccessMessage]        = ???

  val pullImage: F[(Name, Tag), ErrorMessage, SuccessMessage] = ???

  val listAllImages: F[Any, ErrorMessage, SuccessMessage] =
    access.bbbbbb { _ =>
      run0[docker :: images :: all :: `.`]
    }

  val listAllImageIds: F[Any, ErrorMessage, SuccessMessage] =
    access.bbbbbb { _ =>
      run0[docker :: images :: aq :: `.`]
    }

  val removeImage: F[Name | Id, ErrorMessage, SuccessMessage] =
    access.bbbbbb {
      _.fold(
        run1[docker :: rmi :: Name :: `.`](_),
        run1[docker :: rmi :: Id :: `.`](_),
      )
    }

  val removeAllImages: F[Any, ErrorMessage, SuccessMessage] = ???

  private[effect] def run0[Cmd <: HList: Valid: Printed]: F[Any, ErrorMessage, SuccessMessage] =
    cccc.provided(command.executed)(print0[Cmd])

  private[effect] def run1[Cmd <: HList]: runPartialTypeApplication[Cmd, F] =
    new runPartialTypeApplication[Cmd, F]
}

object Docker {

  def apply[F[- _, + _, + _]: Command: aaaaaa: cccccc]: Docker[F] = new Docker[F] {}

  final private[Docker] class runPartialTypeApplication[Cmd <: HList, F[- _, + _, + _]](
    private val d: Boolean = true
  ) extends AnyVal {

    def apply[Tgt, Exp](t: Tgt)(
      implicit
      ev1: Valid[Cmd],
      ev2: Last.Aux[Cmd, Exp],
      ev3: Tgt =:= Exp,
      p: Printed[Cmd],
      command: Command[F],
      cccc: cccccc[F]
    ): F[Any, ErrorMessage, SuccessMessage] =
      cccc.provided(command.executed)(print1[Cmd](t))
  }
}
