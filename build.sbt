import sbt.Keys.testFrameworks

val scala_212       = "2.12.13"
val scala_213       = "2.13.4"
val current_version = scala_213

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
  val cats           = "2.3.0"
  val catsEffect     = "2.5.0"
  val kindProjector  = "0.11.3"
  val munit          = "0.7.25"
  val osLib          = "0.7.4"
  val refined        = "0.9.23"
  val scalaCheck     = "1.15.3"
  val zio            = "1.0.6"
  val zioInteropCats = "2.3.1.0"
  val shapeless      = "2.3.4"
  val silencer       = "1.7.3"
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
  scalaVersion := current_version,
  crossScalaVersions := Seq(scala_212, scala_213),
  libraryDependencies ++= sharedDependencies ++ testDependencies ++ compilerPluginsDependencies,
  organization := "com.github.barambani",
  parallelExecution in Test := false,
  scalacOptions ++=
    (CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 12)) => scala212Options
      case _             => scala213Options
    })
)

lazy val root = project
  .in(file("."))
  .aggregate(api)
  .settings(crossBuildSettings)
  .settings(
    name := "docker-effect",
    publishArtifact := false,
    addCommandAlias("updates", ";dependencyUpdates; reload plugins; dependencyUpdates;reload return"),
    addCommandAlias("fmt", "all scalafmt test:scalafmt scalafmtSbt"),
    addCommandAlias(
      "fmtCheck",
      "all scalafmtCheck test:scalafmtCheck scalafmtSbtCheck"
    ),
    addCommandAlias("fullTest", ";clean;test")
  )

lazy val api = project
  .in(file("api"))
  .settings(crossBuildSettings)
  .settings(
    name := "docker-effect-api",
    libraryDependencies ++= apiDependencies,
    publishArtifact := false,
    testFrameworks += new TestFramework("munit.Framework")
  )
