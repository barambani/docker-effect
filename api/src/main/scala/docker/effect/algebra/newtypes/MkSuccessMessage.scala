package docker.effect
package algebra
package newtypes

import cats.Show
import docker.effect.internal.newtype

object MkSuccessMessage extends newtype[String] with SuccessMessageInstances

private[newtypes] sealed trait SuccessMessageInstances {
  implicit val successMessageShow: Show[SuccessMessage] =
    new Show[SuccessMessage] {
      def show(t: SuccessMessage): String = t.unMk
    }
}
