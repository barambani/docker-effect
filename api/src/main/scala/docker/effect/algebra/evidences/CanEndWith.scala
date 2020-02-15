package docker.effect
package algebra
package evidences

sealed trait CanEndWith[A, B]

object CanEndWith extends CanEndWith2 {
  implicit val evCew1: CanEndWith[docker, images] = _canEndWith[docker, images]
  implicit val evCew2: CanEndWith[docker, ps]     = _canEndWith[docker, ps]

  implicit def evCewIm1[O: VerboseOption: images --| *]: CanEndWith[images, O] = _canEndWith[images, O]
  implicit def evCewPs1[O: VerboseOption: ps --| *]: CanEndWith[ps, O]         = _canEndWith[ps, O]

  implicit def commandTargetEv[A, Tgt: A \\> *]: CanEndWith[A, Tgt] = _canEndWith[A, Tgt]
}

sealed private[algebra] trait CanEndWith2 {
  implicit def evCewIm2[O: CompactOption: images -| *]: CanEndWith[images, O] = _canEndWith[images, O]
  implicit def evCewPs2[O: CompactOption: ps -| *]: CanEndWith[ps, O]         = _canEndWith[ps, O]

  implicit def evCanEndWithOptionArgument[O, Arg: O =| *]: CanEndWith[O, Arg] = _canEndWith[O, Arg]

  implicit def optionTargetEv[A, Tgt: A /\> *]: CanEndWith[A, Tgt] = _canEndWith[A, Tgt]

  final protected def _canEndWith[A, B]: CanEndWith[A, B] = new CanEndWith[A, B] {}
}
