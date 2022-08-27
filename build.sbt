ThisBuild / version := "0.0.1"

ThisBuild / scalaVersion := "3.1.3"

lazy val root = (project in file("."))
  .settings(
    name := "silicon-emporium",
  
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core"    % "2.8.0",
      "org.typelevel" %% "cats-effect"  % "3.3.14",
      "co.fs2"        %% "fs2-core"     % "3.2.12",
      "org.http4s"    %% "http4s-dsl"   % "0.23.15",
      "org.http4s"    %% "http4s-core"  % "0.23.15",
      "org.http4s"    %% "http4s-circe" % "0.23.15",
      "org.http4s"    %% "http4s-ember-server" % "0.23.15",
    )
)
