import _root_.docker.effect.Container
import _root_.docker.effect.algebra._
import _root_.docker.effect.interop.RioApplication
import _root_.docker.effect.syntax.provider._
import cats.effect.Sync
import cats.syntax.functor._
import cats.syntax.show._
import eu.timepit.refined.auto._
import instances.TestRun
import syntax.TestSyntax
import zio.interop.catz._

sealed abstract class ExecutionCheck[F[-_, +_], G[_]](container: Container[F, G])(
  implicit
  ev2: RioApplication[F, G],
  ev4: TestRun[G],
  syn: Sync[G]
) extends munit.FunSuite
    with TestSyntax {

  import container.docker._

  test("docker list images") {
    listAllImages.applied satisfies { res =>
      val resText = res.show
      assert(resText contains "REPOSITORY")
      assert(resText contains "TAG")
      assert(resText contains "IMAGE ID")
      assert(resText contains "CREATED")
      assert(resText contains "SIZE")
    }
  }

  test("docker start redis instance") {
    TestRun.unsafe(
      container.detached(Image("redis"), latest).use { id =>
        listAllContainerIds.applied map (sm => assert(sm.show contains id.value))
      }
    )
  }

  test("docker start redis instance mapping the port") {
    TestRun.unsafe(
      container.detached(Image("redis")).use { id =>
        listAllContainerIds.applied map (sm => assert(sm.show contains id.value))
      }
    )
  }
}

final class ZioExecutionCheck  extends ExecutionCheck(Container.zio)
final class CatsExecutionCheck extends ExecutionCheck(Container.catsIo)
