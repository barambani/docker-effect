package docker.effect
package algebra
package newtypes

import cats.Show
import docker.effect.internal.newtype
import eu.timepit.refined.types.string.NonEmptyString

object MkSuccessMessage
    extends newtype[NonEmptyString]
    with SuccessMessageInstances
    with SuccessMessageFunctions

sealed private[newtypes] trait SuccessMessageInstances {
  implicit val successMessageShow: Show[SuccessMessage] =
    new Show[SuccessMessage] {
      def show(t: SuccessMessage): String = t.unMk.value
    }
}

sealed private[newtypes] trait SuccessMessageFunctions {
  val unsafe: String => SuccessMessage =
    SuccessMessage.apply _ compose NonEmptyString.unsafeFrom
}
