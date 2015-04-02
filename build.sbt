import NativePackagerKeys._

packageArchetype.java_application

name := """scala-getting-started"""

version := "1.0"

scalaVersion := "2.10.4"
  
libraryDependencies ++= Seq(
  "com.twitter" % "finagle-http_2.10" % "6.18.0",
  "io.spray" %%  "spray-json" % "1.3.1"
)
