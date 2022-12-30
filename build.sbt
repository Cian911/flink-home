ThisBuild / resolvers ++= Seq(
    "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
    "MVN Repo" at "https://mvnrepository.com/artifact/",
    "JFrog" at "https://repo.spring.io/ui/native/plugins-release/",
    Resolver.mavenLocal
)

name := "flink-home"

version := "0.1-SNAPSHOT"

organization := "cian911"

ThisBuild / scalaVersion := "2.12.15"

val flinkVersion = "1.15.2"

val flinkDependencies = Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "provided")

// https://mvnrepository.com/artifact/org.apache.flink/flink-clients
libraryDependencies += "org.apache.flink" % "flink-clients" % "1.16.0"


// https://mvnrepository.com/artifact/org.apache.flink/flink-streaming-java
 /*libraryDependencies += "org.apache.flink" % "flink-streaming-java" % flinkVersion */

val AkkaVersion = "2.7.0"

libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-mqtt" % "5.0.0",
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  /* "org.eclipse.paho" % "mqtt-client" % "0.4.0" */
)


// https://mvnrepository.com/artifact/com.hivemq/hivemq-mqtt-client
libraryDependencies += "com.hivemq" % "hivemq-mqtt-client" % "1.3.0"
libraryDependencies += ("org.apache.bahir" % "flink-connector-influxdb_2.12" % "1.1.0").exclude("org.apache.flink", "flink-streaming-java_2.12")
// https://mvnrepository.com/artifact/org.influxdb/influxdb-java
libraryDependencies += "org.influxdb" % "influxdb-java" % "2.23"

lazy val scalaSettings = Seq(
  scalacOptions --= Seq("-Xmx1536M --add-exports=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED")
)

lazy val root = (project in file(".")).
  settings(
    libraryDependencies ++= flinkDependencies,
    scalaSettings
  )

assemblyMergeStrategy in assembly := {
 case PathList("META-INF", _*) => MergeStrategy.discard
 case _                        => MergeStrategy.first
}

assembly / mainClass := Some("cian911.Job")

// make run command include the provided dependencies
Compile / run  := Defaults.runTask(Compile / fullClasspath,
                                   Compile / run / mainClass,
                                   Compile / run / runner
                                  ).evaluated

// stays inside the sbt console when we press "ctrl-c" while a Flink programme executes with "run" or "runMain"
Compile / run / fork := true
Global / cancelable := true

// exclude Scala library from assembly
assembly / assemblyOption  := (assembly / assemblyOption).value.copy(includeScala = false)
