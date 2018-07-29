package docker.effect
package http4s

import cats.effect.IO
import docker.effect.types.{ |, Container, ErrorMessage }
import org.http4s.client.Client

sealed trait Http4sDocker[F[_]] extends Docker[F] {

  private[http4s] val client: Client[F]

  def createDefault: (Container.Name, Container.Image) => F[ErrorMessage | Container.Created] = ???
}

object Http4sDocker {

  implicit def ioHttp4sDocker(implicit ev: Client[IO]): Http4sDocker[IO] =
    new Http4sDocker[IO] { val client = ev }
}
