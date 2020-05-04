import sbt.Keys.testFrameworks
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

lazy val scala212Options = Seq(
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
  "-Xfatal-warnings",
  "-opt:l:inline",
  "-Ywarn-unused:imports",
  "-Ywarn-unused:_,imports",
  "-opt-warnings",
  "-Xlint:constant",
  "-Ywarn-extra-implicit",
  "-opt-inline-from:<source>"
)

lazy val scala213Options = scala212Options diff Seq(
  "-Ywarn-nullary-override",
  "-Ypartial-unification",
  "-Ywarn-nullary-unit",
  "-Ywarn-inaccessible",
  "-Ywarn-infer-any",
  "-Yno-adapted-args",
  "-Xfuture"
)

lazy val versionOf = new {
  val cats           = "2.1.1"
  val catsEffect     = "2.1.3"
  val kindProjector  = "0.11.0"
  val munit          = "0.7.5"
  val osLib          = "0.7.0"
  val refined        = "0.9.14"
  val scalaCheck     = "1.14.3"
  val zio            = "1.0.0-RC18-2"
  val zioInteropCats = "2.0.0.0-RC13"
  val shapeless      = "2.3.3"
  val silencer       = "1.7.0"
}

lazy val sharedDependencies = Seq(
  "com.github.ghik" %% "silencer-lib" % versionOf.silencer % Provided cross CrossVersion.full
)

lazy val compilerPluginsDependencies = Seq(
  compilerPlugin(
    "org.typelevel" %% "kind-projector" % versionOf.kindProjector cross CrossVersion.full
  ),
  compilerPlugin(
    "com.github.ghik" %% "silencer-plugin" % versionOf.silencer cross CrossVersion.full
  )
)

lazy val testDependencies = Seq(
  "org.scalameta"  %% "munit"            % versionOf.munit      % Test,
  "org.scalameta"  %% "munit-scalacheck" % versionOf.munit      % Test,
  "org.scalacheck" %% "scalacheck"       % versionOf.scalaCheck % Test
)

lazy val apiDependencies = Seq(
  "org.typelevel" %% "cats-core"        % versionOf.cats,
  "org.typelevel" %% "cats-effect"      % versionOf.catsEffect,
  "com.lihaoyi"   %% "os-lib"           % versionOf.osLib,
  "eu.timepit"    %% "refined"          % versionOf.refined,
  "com.chuusai"   %% "shapeless"        % versionOf.shapeless,
  "dev.zio"       %% "zio"              % versionOf.zio,
  "dev.zio"       %% "zio-interop-cats" % versionOf.zioInteropCats
) map (_.withSources)

lazy val crossBuildSettings = Seq(
  libraryDependencies ++= sharedDependencies ++ testDependencies ++ compilerPluginsDependencies,
  organization := "com.github.barambani",
  parallelExecution in Test := false,
  scalacOptions ++=
    (CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 12)) => scala212Options
      case _             => scala213Options
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
  releaseCrossBuild := true,
  publishMavenStyle := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  licenses := Seq(
    "MIT License" ->
      url("https://raw.githubusercontent.com/barambani/docker-effect/master/LICENSE")
  ),
  homepage := Some(url("https://github.com/barambani/http4s-extend")),
  publishTo := sonatypePublishTo.value,
  pomExtra :=
    <scm>
      <url>https://github.com/barambani/docker-effect</url>
      <connection>scm:git:git@github.com:barambani/docker-effect.git</connection>
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
  .aggregate(api)
  .settings(crossBuildSettings)
  .settings(releaseSettings)
  .settings(
    name := "docker-effect",
    publishArtifact := false,
    addCommandAlias("format", ";scalafmt;test:scalafmt;scalafmtSbt"),
    addCommandAlias("updates", ";dependencyUpdates; reload plugins; dependencyUpdates;reload return"),
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
    name := "docker-effect-api",
    libraryDependencies ++= apiDependencies,
    publishArtifact := false,
    testFrameworks += new TestFramework("munit.Framework")
  )
