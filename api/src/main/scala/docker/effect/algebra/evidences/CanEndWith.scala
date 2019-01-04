package docker
package effect
package algebra
package evidences

import docker.effect.algebra.algebra._

sealed trait CanEndWith[A, B]

object CanEndWith extends CanEndWith2 {

  implicit def evCew1[O: VerboseOption: images --| ?]: CanEndWith[images, O] = _evidenceOf[images, O]
  implicit def evCew2[O: VerboseOption: ps --| ?]: CanEndWith[ps, O]         = _evidenceOf[ps, O]

  implicit def targetEv[A, Tgt: A \\> ?]: CanEndWith[A, Tgt] = _evidenceOf[A, Tgt]
}

sealed private[algebra] trait CanEndWith2 {

  implicit val evCew1: CanEndWith[docker, images] = _evidenceOf[docker, images]
  implicit val evCew2: CanEndWith[docker, ps]     = _evidenceOf[docker, ps]

  implicit def evCew3[O: CompactOption: images -| ?]: CanEndWith[images, O] = _evidenceOf[images, O]
  implicit def evCew4[O: CompactOption: ps -| ?]: CanEndWith[ps, O]         = _evidenceOf[ps, O]
  implicit def evCew5[O, Arg: O =| ?]: CanEndWith[O, Arg]                   = _evidenceOf[O, Arg]

  final protected def _evidenceOf[A, B]: CanEndWith[A, B] = new CanEndWith[A, B] {}
}
