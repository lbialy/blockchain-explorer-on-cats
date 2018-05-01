val Http4sVersion = "0.18.9"
val Specs2Version = "4.1.0"
val LogbackVersion = "1.2.3"

lazy val root = (project in file("."))
  .settings(
    organization := "com.example",
    name := "blockchain-explorer-on-cats",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.5",
    libraryDependencies ++= Seq(
      "org.http4s"            %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"            %% "http4s-blaze-client" % "0.18.9-GraalVM",
      "org.http4s"            %% "http4s-circe"        % Http4sVersion,
      "org.http4s"            %% "http4s-dsl"          % Http4sVersion,
      "org.http4s"            %% "http4s-circe"        % Http4sVersion,
      "io.circe"              %% "circe-generic"       % "0.9.3",
      "org.specs2"            %% "specs2-core"         % Specs2Version % "test",
      "ch.qos.logback"        %  "logback-classic"     % LogbackVersion
    ),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
  )

