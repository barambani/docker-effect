final class IncorrectCommandsCheck extends munit.FunSuite {
  test("docker kill not compiling") {
    assertNoDiff(
      compileErrors("""
        import docker.effect._
        import docker.effect.algebra._
        import shapeless.{::, HNil}
        print0[docker :: kill :: `.`]"""),
      """|error: not found: value print0
         |        print0[docker :: kill :: `.`]
         |        ^
         |""".stripMargin
    )
  }

  test("docker rmi not compiling") {
    assertNoDiff(
      compileErrors("""
        import docker.effect._
        import docker.effect.algebra._
        import shapeless.{::, HNil}
        print0[docker :: rmi :: `.`]"""),
      """|error: not found: value print0
         |        print0[docker :: rmi :: `.`]
         |        ^
         |""".stripMargin
    )
  }

  test("docker rmi by id not compiling") {
    assertNoDiff(
      compileErrors("""
        import docker.effect._
        import docker.effect.algebra._
        import shapeless.{::, HNil}
        print1[docker :: rmi :: Id :: `.`](fd484f19954f)"""),
      """|error: not found: value print1
         |        print1[docker :: rmi :: Id :: `.`](fd484f19954f)
         |        ^
         |""".stripMargin
    )
  }

  test("docker rmi by repo not compiling") {
    assertNoDiff(
      compileErrors("""
        import docker.effect._
        import docker.effect.algebra._
        import shapeless.{::, HNil}
        print1[docker :: rmi :: Repo :: `.`]("test-repo")"""),
      """|error: not found: value print1
         |        print1[docker :: rmi :: Repo :: `.`]("test-repo")
         |        ^
         |""".stripMargin
    )
  }

  test("kill signal KILL compiling") {
    assertNoDiff(
      compileErrors("""
        import docker.effect._
        import docker.effect.algebra._
        import shapeless.{::, HNil}
        print1[kill :: signal :: KILL :: `.`]("test-repo")"""),
      """|error: not found: value print1
         |        print1[kill :: signal :: KILL :: `.`]("test-repo")
         |        ^
         |""".stripMargin
    )
  }
}
