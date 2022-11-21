ThisBuild / version := "0.1.3"

ThisBuild / scalaVersion := "3.2.0"

lazy val root = (project in file("."))
  .enablePlugins(JavaServerAppPackaging, DockerPlugin)
  .settings(
    name := "silicon-emporium-backend",
    dockerRepository := Some("ghcr.io"),
    dockerUsername := Some("hombre-x"),
    dockerExposedPorts += sys.env.get("SE_API_HTTP_PORT").map(_.toInt).getOrElse(8000),
    dockerBaseImage := "openjdk:19",
    libraryDependencies ++= Seq(

      // Cats
      "org.typelevel" %% "cats-core" % "2.9.0",
      "org.typelevel" %% "cats-effect" % "3.4.0",
      "org.typelevel" %% "log4cats-core" % "2.5.0",
      "org.typelevel" %% "log4cats-slf4j" % "2.5.0",

      // Fs2
      "co.fs2" %% "fs2-core" % "3.3.0",

      // Circe
      "io.circe" %% "circe-generic" % "0.14.3",
      "io.circe" %% "circe-parser" % "0.14.3",

      // SQL
      "org.tpolecat" %% "skunk-core" % "0.3.2",

      // http4s
      "org.http4s" %% "http4s-dsl"          % "0.23.16",
      "org.http4s" %% "http4s-core"         % "0.23.16",
      "org.http4s" %% "http4s-circe"        % "0.23.16",
      "org.http4s" %% "http4s-ember-server" % "0.23.16",
      "org.http4s" %% "http4s-ember-client" % "0.23.16",

      // Profunktor
//      "dev.profunktor" % "http4s-jwt-auth"    % "1.0.0",

      // Refined Types
      "eu.timepit" %% "refined" % "0.10.1",

      // Ciris
      "is.cir" %% "ciris" % "3.0.0",

      // Auth
      "com.dedipresta" % "scala-crypto" % "1.0.0" cross CrossVersion.for3Use2_13,

      // Testing
      "org.scalatest"       %% "scalatest"   % "3.2.14" % "test",
      "com.disneystreaming" %% "weaver-cats" % "0.8.0"  % "test",

      // Java libs
      "ch.qos.logback" % "logback-classic" % "1.4.4",
      "org.jsoup"      % "jsoup"           % "1.15.3"
    ),
    scalacOptions ++= Seq(
    )
  )
