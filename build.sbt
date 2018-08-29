import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

lazy val `scala 211` = "2.11.12"
lazy val `scala 212` = "2.12.6"

/**
  * Scalac options
  */
lazy val crossBuildOptions = Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-explaintypes",
  "-Yrangepos",
  "-feature",
  "-Xfuture",
  "-Ypartial-unification",
  "-language:higherKinds",
  "-language:existentials",
  "-unchecked",
  "-Yno-adapted-args",
  "-Xlint:_,-type-parameter-shadow",
  "-Xsource:2.13",
  "-Ywarn-dead-code",
  "-Ywarn-inaccessible",
  "-Ywarn-infer-any",
  "-Ywarn-nullary-override",
  "-Ywarn-nullary-unit",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfatal-warnings"
)

lazy val scala212Options = Seq(
  "-opt:l:inline",
  "-Ywarn-unused:imports",
  "-Ywarn-unused:_,imports",
  "-opt-warnings",
  "-Xlint:constant",
  "-Ywarn-extra-implicit",
  "-opt-inline-from:<source>"
)

/**
  * Dependencies
  */
lazy val versionOf = new {
  val cats          = "1.2.0"
  val shapeless     = "2.3.3"
  val refined       = "0.9.2"
  val http4s        = "0.18.16"
  val scalaCheck    = "1.14.0"
  val scalaTest     = "3.0.5"
  val kindProjector = "0.9.7"
  val silencer      = "1.2"
  val typedapi      = "0.2.0-RC1"
  val circe         = "0.9.3"
  val unixSocket    = "0.19"
}

lazy val sharedDependencies = Seq(
  "com.github.ghik" %% "silencer-lib" % versionOf.silencer
) map (_.withSources)

lazy val apiDependencies = Seq(
  "com.chuusai"         %% "shapeless"              % versionOf.shapeless,
  "eu.timepit"          %% "refined"                % versionOf.refined,
  "com.github.pheymann" %% "typedapi-client"        % versionOf.typedapi,
  "com.github.pheymann" %% "typedapi-server"        % versionOf.typedapi,
  "com.github.pheymann" %% "typedapi-http4s-client" % versionOf.typedapi,
  "io.circe"            %% "circe-generic"          % versionOf.circe
) map (_.withSources)

lazy val http4sDependencies = Seq(
  "org.typelevel"  %% "cats-core"           % versionOf.cats,
  "org.http4s"     %% "http4s-dsl"          % versionOf.http4s,
  "org.http4s"     %% "http4s-blaze-server" % versionOf.http4s,
  "org.http4s"     %% "http4s-blaze-client" % versionOf.http4s,
  "org.http4s"     %% "http4s-circe"        % versionOf.http4s
) map (_.withSources)

lazy val testDependencies = Seq(
  "org.scalacheck" %% "scalacheck" % versionOf.scalaCheck % Test,
  "org.scalatest"  %% "scalatest"  % versionOf.scalaTest  % Test
)

lazy val compilerPluginsDependencies = Seq(
  compilerPlugin(
    "org.spire-math" %% "kind-projector" % versionOf.kindProjector cross CrossVersion.binary
  ),
  compilerPlugin("com.github.ghik" %% "silencer-plugin" % versionOf.silencer)
)

/**
  * Settings
  */
lazy val crossBuildSettings = Seq(
  scalaVersion              := `scala 212`,
  crossScalaVersions        := Seq(`scala 211`, `scala 212`),
  scalacOptions             ++= crossBuildOptions,
  libraryDependencies       ++= sharedDependencies ++ testDependencies ++ compilerPluginsDependencies,
  organization              := "io.laserdisc",
  parallelExecution in Test := false,
  scalacOptions ++=
    (scalaVersion.value match {
      case `scala 212` => scala212Options
      case _           => Seq()
    })
)

lazy val releaseSettings: Seq[Def.Setting[_]] = Seq(
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    publishArtifacts,
    setNextVersion,
    commitNextVersion,
    releaseStepCommand("sonatypeRelease"),
    pushChanges
  ),
  releaseCrossBuild             := true,
  publishMavenStyle             := true,
  credentials                   := Credentials(Path.userHome / ".ivy2" / ".credentials") :: Nil,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  publishArtifact in Test       := false,
  pomIncludeRepository := { _ =>
    false
  },
  licenses := Seq(
    "MIT License" ->
      url("https://raw.githubusercontent.com/laserdisc-io/docker-effect/master/LICENSE")
  ),
  homepage  := Some(url("http://laserdisc.io")),
  publishTo := sonatypePublishTo.value,
  pomExtra :=
    <scm>
      <url>https://github.com/laserdisc-io/docker-effect/tree/master</url>
      <connection>scm:git:git@github.com:laserdisc-io/docker-effect.git</connection>
    </scm>
    <developers>
      <developer>
        <id>barambani</id>
        <name>Filippo Mariotti</name>
        <url>https://github.com/barambani</url>
      </developer>
    </developers>
)

lazy val root = project
  .in(file("."))
  .aggregate(api, http4s)
  .settings(crossBuildSettings)
  .settings(releaseSettings)
  .settings(
    name            := "docker-effect",
    publishArtifact := false,
    addCommandAlias("format", ";scalafmt;test:scalafmt;scalafmtSbt"),
    addCommandAlias(
      "checkFormat",
      ";scalafmtCheck;test:scalafmtCheck;scalafmtSbtCheck"
    ),
    addCommandAlias("fullCiBuild", ";checkFormat;clean;test")
  )

lazy val api = project
  .in(file("api"))
  .settings(crossBuildSettings)
  .settings(releaseSettings)
  .settings(
    name                := "docker-effect-api",
    libraryDependencies ++= apiDependencies,
    publishArtifact     := false
  )

lazy val http4s = project
  .in(file("http4s"))
  .dependsOn(api)
  .settings(crossBuildSettings)
  .settings(releaseSettings)
  .settings(
    name                := "docker-effect-htt4s",
    libraryDependencies ++= http4sDependencies
  )
