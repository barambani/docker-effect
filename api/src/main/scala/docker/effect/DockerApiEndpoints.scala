package docker.effect

import docker.effect.types.Container

object DockerApiEndpoints {

  import typedapi.dsl._

  val createContainerEp =
    := :> "containers" :> "create" :> Query[Container.Name]("name") :>
      ReqBody[Json, Container.Create] :> Post[Json, Container.Created]

  val startContainerByIdEp =
    := :> "containers" :> Segment[Container.Id]("id") :> "start" :> Post[Json, Unit]

  val startContainerByNameEp =
    := :> "containers" :> Segment[Container.Name]("id") :> "start" :> Post[Json, Unit]
}
