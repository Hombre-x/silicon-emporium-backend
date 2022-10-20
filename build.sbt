ThisBuild / version := "0.0.1"

ThisBuild / scalaVersion := "3.2.0"

lazy val root = (project in file("."))
  .settings(
    name := "silicon-emporium",
    libraryDependencies ++= Seq(
      // Cats
      "org.typelevel" %% "cats-core"           % "2.8.0",
      "org.typelevel" %% "cats-effect"         % "3.3.14",
      "org.typelevel" %% "log4cats-core"       % "2.5.0",
      "org.typelevel" %% "log4cats-slf4j"      % "2.5.0",
      
      // Fs2
      "co.fs2"        %% "fs2-core"            % "3.3.0",
      
      // Circe
      "io.circe"      %% "circe-generic"       % "0.14.3",
      
      // http4s
      "org.http4s"    %% "http4s-dsl"          % "0.23.16",
      "org.http4s"    %% "http4s-core"         % "0.23.16",
      "org.http4s"    %% "http4s-circe"        % "0.23.16",
      "org.http4s"    %% "http4s-ember-server" % "0.23.16",
      "org.http4s"    %% "http4s-ember-client" % "0.23.16",
      
      // Refined Types
      "eu.timepit"    %% "refined"             % "0.10.1",
      
      // Testing
      "org.scalatest" %% "scalatest"           % "3.2.14" % "test",
      // Java libs
      "ch.qos.logback" % "logback-classic"     % "1.4.3",
      "org.jsoup"      % "jsoup"               % "1.15.3"
    ),

    scalacOptions ++= Seq(
      "-Ywarn-unused",
      
    )
  )
