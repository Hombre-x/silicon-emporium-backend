ThisBuild / version := "0.0.1"

ThisBuild / scalaVersion := "3.2.0"

lazy val root = (project in file("."))
  .settings(
    name := "silicon-emporium",
    libraryDependencies ++= Seq(
      // Cats
      "org.typelevel" %% "cats-core"           % "2.8.0",
      "org.typelevel" %% "cats-effect"         % "3.3.14",
      "org.typelevel" %% "log4cats-core"       % "2.4.0",
      "org.typelevel" %% "log4cats-slf4j"      % "2.4.0",
      // Fs2
      "co.fs2"        %% "fs2-core"            % "3.2.14",
      // Circe
      "io.circe"      %% "circe-generic"       % "0.14.2",
      // http4s
      "org.http4s"    %% "http4s-dsl"          % "0.23.15",
      "org.http4s"    %% "http4s-core"         % "0.23.15",
      "org.http4s"    %% "http4s-circe"        % "0.23.15",
      "org.http4s"    %% "http4s-ember-server" % "0.23.15",
      // Refined Types
      "eu.timepit"    %% "refined"             % "0.10.1",
      "eu.timepit"    %% "refined-cats"        % "0.10.1",
      // Java libs
      "ch.qos.logback" % "logback-classic"     % "1.4.0",
      "org.jsoup"      % "jsoup"               % "1.15.3"
    )
  )
