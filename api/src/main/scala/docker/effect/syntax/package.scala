package docker.effect

package object syntax {
  object nes            extends NonEmptyStringSyntax
  object provider       extends ProviderSyntax
  object commands       extends DockerCommandSyntax
  object successMessage extends SuccessMessageSyntax
  object rio            extends RioSyntax
}
