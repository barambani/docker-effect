package docker
package effect
package algebra

import cats.Show
import docker.effect.algebra.algebra.SuccessMessage
import docker.effect.internal.newtype
import eu.timepit.refined.types.string.NonEmptyString

object MkSuccessMessage extends newtype[NonEmptyString] with SuccessMessageInstances

sealed private[algebra] trait SuccessMessageInstances {

  implicit val successMessageShow: Show[SuccessMessage] =
    new Show[SuccessMessage] {
      def show(t: SuccessMessage): String = t.unMk.value
    }
}
