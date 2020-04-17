package docker.effect
package algebra
package evidences

sealed trait IsFinal[A, B]

object IsFinal extends CanEndWith2 {
  implicit val evCew1: IsFinal[docker, images] = _isFinal[docker, images]
  implicit val evCew2: IsFinal[docker, ps]     = _isFinal[docker, ps]

  implicit def evCewIm1[VO: VerboseOption: images AcceptsVerboseOpt *]: IsFinal[images, VO] =
    _isFinal[images, VO]
  implicit def evCewPs1[VO: VerboseOption: ps AcceptsVerboseOpt *]: IsFinal[ps, VO] =
    _isFinal[ps, VO]

  implicit def commandTargetEv[A, Tgt: A AcceptsCmdTarget *]: IsFinal[A, Tgt] =
    _isFinal[A, Tgt]
}

sealed private[algebra] trait CanEndWith2 {
  implicit def evCewIm2[CO: CompactOption: images AcceptsCompactOpt *]: IsFinal[images, CO] =
    _isFinal[images, CO]
  implicit def evCewPs2[CO: CompactOption: ps AcceptsCompactOpt *]: IsFinal[ps, CO] =
    _isFinal[ps, CO]

  implicit def evCanEndWithOptionArgument[O, Arg: O AcceptsArgument *]: IsFinal[O, Arg] =
    _isFinal[O, Arg]

  implicit def optionTargetEv[A, Tgt: A AcceptsOptTarget *]: IsFinal[A, Tgt] =
    _isFinal[A, Tgt]

  final protected def _isFinal[A, B]: IsFinal[A, B] = new IsFinal[A, B] {}
}
