package docker
package effect
package algebra
package newtypes

import cats.Show
import docker.effect.internal.newtype
import eu.timepit.refined.types.string.NonEmptyString

object MkErrorMessage extends newtype[NonEmptyString] with ErrorMessageInstances

sealed private[algebra] trait ErrorMessageInstances {

  implicit val errorMessageShow: Show[ErrorMessage] =
    new Show[ErrorMessage] {
      def show(t: ErrorMessage): String = t.unMk.value
    }
}
