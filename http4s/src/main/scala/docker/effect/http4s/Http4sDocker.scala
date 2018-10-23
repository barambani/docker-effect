//package docker
//package effect
//package http4s
//
//import cats.data.EitherT
//import cats.data.EitherT.{ left, leftT, right, rightT }
//import cats.effect.{ Effect, Sync }
//import cats.syntax.applicativeError._
//import cats.syntax.apply._
//import cats.syntax.functor._
//import docker.effect.DockerApiEndpoints._
//import docker.effect.types.Container.WaitBeforeKill
//import docker.effect.types.{ ErrorMessage, _ }
//import io.circe.generic.auto._
//import org.http4s.Status._
//import org.http4s.circe.{ jsonEncoderOf, jsonOf }
//import org.http4s.client.Client
//import org.http4s.client.blaze.Http1Client
//import typedapi.client._
//import typedapi.client.http4s._
//import internal.syntax._
//
//sealed abstract class Http4sDocker[F[_]: Effect](client: Client[F], h: EngineHost, p: EnginePort)
//    extends MaterializedApi[F]
//    with Docker[λ[(A, B) => EitherT[F, A, B]]] {
//
//  final type G[A, B] = EitherT[F, A, B]
//
//  final private val clientManager =
//    ClientManager(client, h.value, p.value)
//
//  implicit private val errorDecoder = jsonOf[F, ErrorMessage]
//
//  def startUnixSocketRelay: G[ErrorMessage, Unit] = {
//    import sys.process._
//
//    val F = Sync[F]
//    val startSocketRelay =
//      s"""docker run -d --name docker-effect-socket-relay -v /var/run/docker.sock:/var/run/docker.sock -p 127.0.0.1:$p:$p bobrik/socat TCP-LISTEN:$p,fork UNIX-CONNECT:/var/run/docker.sock"""
//        .stripMargin
//
//    (F.delay(startSocketRelay.!) *> F.unit).attemptT leftMap (th => ErrorMessage(s"Exception: $th"))
//  }
//
//  def cleanUnixSocketRelay: G[ErrorMessage, Unit] = {
//    import sys.process._
//
//    val F                 = Sync[F]
//    val killSocketRelay   = "docker kill docker-effect-socket-relay"
//    val removeSocketRelay = "docker rm docker-effect-socket-relay"
//
//    (F.delay(killSocketRelay.!) *> F.delay(removeSocketRelay.!) *> F.unit).attemptT leftMap (
//      th => ErrorMessage(s"Exception: $th")
//    )
//  }
//
//  def createContainer: (Container.Name, Image.Name) => G[ErrorMessage, Container.Created] =
//    (cn, in) => {
//
//      implicit val encoder = jsonEncoderOf[F, Container.Create]
//      implicit val decoder = jsonOf[F, Container.Created]
//
//      createContainerC(cn, Container.Create(in))
//        .run[F]
//        .raw(clientManager)
//        .handleWith {
//          case Created(r)    => right(r.as[Container.Created])
//          case BadRequest(_) => leftT(ErrorMessage(s"400 -> Bad parameter."))
//          case Conflict(_)   => leftT(ErrorMessage(s"409 -> Conflict."))
//          case other         => left(other.as[ErrorMessage])
//        }
//    }
//
//  def startContainer: Container.Id | Container.Name => G[ErrorMessage, Unit] =
//    eitherNameOrId => {
//
//      val response = eitherNameOrId.fold(
//        id => startContainerByIdC(id).run[F].raw(clientManager),
//        name => startContainerByNameC(name).run[F].raw(clientManager)
//      )
//
//      response.handleWith {
//        case NoContent(_)   => rightT(())
//        case NotModified(_) => leftT(ErrorMessage(s"304 -> Container already started."))
//        case other          => left(other.as[ErrorMessage])
//      }
//    }
//
//  def stopContainer: Container.Id | Container.Name => G[ErrorMessage, Unit]                           = ???
//  def stopContainerSchedule: (Container.Id | Container.Name, WaitBeforeKill) => G[ErrorMessage, Unit] = ???
//
//  def waitContainerStop: Container.Id | Container.Name => G[ErrorMessage, Unit] = ???
//
//  def restartContainer: Container.Id | Container.Name => G[ErrorMessage, Unit]                           = ???
//  def restartContainerSchedule: (Container.Id | Container.Name, WaitBeforeKill) => G[ErrorMessage, Unit] = ???
//
//  def killContainer: Container.Id | Container.Name => G[ErrorMessage, Unit] =
//    eitherNameOrId => {
//
//      val response = eitherNameOrId.fold(
//        id => killContainerByIdC(id).run[F].raw(clientManager),
//        name => killContainerByNameC(name).run[F].raw(clientManager)
//      )
//
//      response.handleWith {
//        case NoContent(_) => rightT(())
//        case other        => left(other.as[ErrorMessage])
//      }
//    }
//
//  def removeContainer: Container.Id | Container.Name => G[ErrorMessage, Unit] =
//    eitherNameOrId => {
//
//      val response = eitherNameOrId.fold(
//        id => removeContainerByIdC(id).run[F].raw(clientManager),
//        name => removeContainerByNameC(name).run[F].raw(clientManager)
//      )
//
//      response.handleWith {
//        case NoContent(_) => rightT(())
//        case other        => left(other.as[ErrorMessage])
//      }
//    }
//
//  def forceRemoveContainer: Container.Id | Container.Name => G[ErrorMessage, Unit] = ???
//
//  def removeContainerAndVolumes: Container.Id | Container.Name => G[ErrorMessage, Unit] = ???
//
//  def pullImage: (Image.Repo, Image.Name, Image.Tag) => G[ErrorMessage, String] =
//    (ir, in, it) =>
//      pullImageC(ir, in, it)
//        .run[F]
//        .raw(clientManager)
//        .handleWith {
//          case Ok(r)       => rightT(r.as[Container.Created])
//          case NotFound(_) => leftT(ErrorMessage(s"404 -> repository does not exist or no read access."))
//          case other       => left(other.as[ErrorMessage])
//      }
//
//  def removeImage: Image.Id | Image.Name => G[ErrorMessage, Unit] =
//    eitherNameOrId => {
//
//      val response = eitherNameOrId.fold(
//        id => removeImageByIdC(id).run[F].raw(clientManager),
//        name => removeImageByNameC(name).run[F].raw(clientManager)
//      )
//
//      response.handleWith {
//        case Ok(_)       => rightT(())
//        case NotFound(_) => leftT(ErrorMessage(s"404 -> No such image."))
//        case Conflict(_) => leftT(ErrorMessage(s"409 -> Conflict."))
//        case other       => left(other.as[ErrorMessage])
//      }
//    }
//}
//
//abstract class MaterializedApi[F[_]: Sync] {
//
//  protected val (
//    createContainerC,
//    startContainerByIdC,
//    startContainerByNameC,
//    killContainerByIdC,
//    killContainerByNameC,
//    removeContainerByIdC,
//    removeContainerByNameC,
//    pullImageC,
//    removeImageByIdC,
//    removeImageByNameC
//  ) =
//    deriveAll(
//      createContainerEp :|:
//        startContainerByIdEp :|:
//        startContainerByNameEp :|:
//        killContainerByIdEp :|:
//        killContainerByNameEp :|:
//        removeContainerByIdEp :|:
//        removeContainerByNameEp :|:
//        pullImageEp :|:
//        removeImageByIdEp :|:
//        removeImageByNameEp
//    )
//}
//
//object Http4sDocker {
//
//  def apply[F[_]: Effect](cl: Client[F])(h: EngineHost, p: EnginePort): Http4sDocker[F] =
//    new Http4sDocker[F](cl, h, p) {}
//
//  def apply[F[_]: Effect](h: EngineHost, p: EnginePort): F[Http4sDocker[F]] =
//    Http1Client[F]() map (Http4sDocker[F](_)(h, p))
//
//  def setupUnixSocketRelay[F[_]: Effect](docker: Http4sDocker[F]): EitherT[F, ErrorMessage, Unit] =
//    docker.startUnixSocketRelay
//
//  def cleanupUnixSocketRelay[F[_]: Effect](
//    docker: Http4sDocker[F]
//  ): EitherT[F, ErrorMessage, Unit] =
//    docker.cleanUnixSocketRelay
//}
