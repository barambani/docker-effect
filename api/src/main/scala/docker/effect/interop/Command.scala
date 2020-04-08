package docker.effect
package interop

import docker.effect.algebra.{ DockerCommand, SuccessMessage }
import docker.effect.syntax.commands._
import zio.RIO

sealed trait Command[F[-_, +_]] {
  def executed: F[DockerCommand, SuccessMessage]
}

object Command {

  implicit val rioCommand: Command[RIO] =
    new Command[RIO] {
      def executed: RIO[DockerCommand, SuccessMessage] =
        RIO.accessM { cmd =>
          RIO.effect(
            os.proc(cmd.words)
              .call(
                stdin = os.Pipe,
                stdout = os.Pipe,
                stderr = os.Pipe
              )
          ) >>= { res =>
            if (res.exitCode == 0) {
              val successMessage = res.out.trim
              if (successMessage.isEmpty)
                RIO.succeed(SuccessMessage.unsafe(s"Command `${cmd.unMk.value}` execution complete"))
              else RIO.succeed(SuccessMessage.unsafe(successMessage))
            } else RIO.fail(new RuntimeException(res.err.trim))
          }
        }
    }
}
