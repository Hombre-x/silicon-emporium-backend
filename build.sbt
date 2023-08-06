ThisBuild / version := "0.1.15"

ThisBuild / scalaVersion := "3.3.0"

ThisBuild / semanticdbEnabled := true

ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

lazy val root = (project in file("."))
  .enablePlugins(JavaServerAppPackaging, DockerPlugin)
  .settings(
    name             := "silicon-emporium-backend",
    dockerRepository := Some("ghcr.io"),
    dockerUsername   := Some("hombre-x"),
    dockerExposedPorts ++= Seq(80, 8000, 8080),
    dockerBaseImage  := "openjdk:19",
    libraryDependencies ++= Seq(
      
      // Cats
      "org.typelevel" %% "cats-core"      % "2.9.0",
      "org.typelevel" %% "cats-effect"    % "3.5.0",
      "org.typelevel" %% "log4cats-core"  % "2.5.0",
      "org.typelevel" %% "log4cats-slf4j" % "2.5.0",

      // Fs2
      "co.fs2" %% "fs2-core" % "3.7.0",

      // Circe
      "io.circe" %% "circe-generic" % "0.14.5",
      "io.circe" %% "circe-parser"  % "0.14.5",

      // SQL
      "org.tpolecat" %% "skunk-core" % "0.5.1",

      // http4s
      "org.http4s" %% "http4s-dsl"          % "0.23.18",
      "org.http4s" %% "http4s-core"         % "0.23.18",
      "org.http4s" %% "http4s-circe"        % "0.23.19",
      "org.http4s" %% "http4s-ember-server" % "0.23.18",
      "org.http4s" %% "http4s-ember-client" % "0.23.18",

      // Ciris
      "is.cir" %% "ciris" % "3.1.0",

      // Auth
      "com.dedipresta" % "scala-crypto" % "1.0.0" cross CrossVersion.for3Use2_13,

      // Testing
      "org.scalatest" %% "scalatest" % "3.2.15" % "test",

      // Java libs
      "ch.qos.logback" % "logback-classic" % "1.4.7",
      "org.jsoup"      % "jsoup"           % "1.15.4"
    ),
    
    scalacOptions ++= Seq(
      "-Wunused:all",
      "-deprecation"
    )
  )
