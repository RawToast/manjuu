val scala3Version = "3.4.0"

val Http4sVersion     = "0.23.22"
val LogbackVersion    = "1.2.6"
val EdoMataVersion    = "0.11.1"
val CirceVersion      = "0.14.5"
val SkunkVersion      = "0.6.0"
val MonocleVersion    = "3.2.0"
val CatsEffect        = "3.5.4"
val CatsVersion       = "2.10.0"
val Redis4CatsVersion = "1.6.0"

val MunitVersion           = "0.7.29"
val MunitCatsEffectVersion = "1.0.7"

lazy val root = project
  .in(file("."))
  .settings(
    name := "manjuu-backend",
    scalaVersion := scala3Version,
    assembly / mainClass := Some("manjuu.Server"),
    assembly / assemblyJarName := "manjuu-backend.jar",
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", _*) => MergeStrategy.discard
      case _                        => MergeStrategy.first
    },
    libraryDependencies ++= Seq(
      // Http4s server
      "org.http4s" %% "http4s-ember-server" % Http4sVersion,
      "org.http4s" %% "http4s-ember-client" % Http4sVersion,
      "org.http4s" %% "http4s-circe"        % Http4sVersion,
      "org.http4s" %% "http4s-dsl"          % Http4sVersion,

      // Circe (Json)
      "io.circe" %% "circe-core"    % CirceVersion,
      "io.circe" %% "circe-literal" % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-parser"  % CirceVersion,
      "io.circe" %% "circe-optics"  % "0.15.0",

      // Redis
      "dev.profunktor" %% "redis4cats-effects" % Redis4CatsVersion,

      // Monocle lenses
      "dev.optics" %% "monocle-core"  % MonocleVersion,
      "dev.optics" %% "monocle-macro" % MonocleVersion,

      // Logging
      "ch.qos.logback" % "logback-classic" % LogbackVersion,

      // Config
      "com.typesafe" % "config" % "1.4.3",
      // "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",

      // Test
      "org.scalameta" %% "munit"               % MunitVersion           % Test,
      "org.typelevel" %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test
    ),
    testFrameworks += new TestFramework("munit.Framework"),
    run / fork := true,
    scalacOptions ++= Seq(
      "-deprecation", // emit warning and location for usages of deprecated APIs
      // "-explain", // explain errors in more detail
      // "-explain-types", // explain type errors in more detail
      "-feature", // emit warning and location for usages of features that should be imported explicitly
      "-indent", // allow significant indentation.
      "-new-syntax", // require `then` and `do` in control expressions.
      "-print-lines", // show source code line numbers.
      "-unchecked", // enable additional warnings where generated code depends on assumptions
      "-Ykind-projector", // allow `*` as wildcard to be compatible with kind projector
      "-Xmigration" // warn about constructs whose behavior may have changed since version
    )
  )
addCommandAlias(
  "codeCoverage",
  "clean; coverage ; test ; coverageReport"
)

// addCommandAlias("validate", ";coverage;test;coverageReport")
