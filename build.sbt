organization := "com.gvolpe"

name := """simple-akka-streams"""

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-experimental" % "1.0-RC4"
)