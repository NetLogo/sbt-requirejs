version := "1.1"

sbtPlugin := true

scalaVersion in ThisBuild := "2.10.2"

name := "sbt-requirejs"

organization := "org.nlogo"

libraryDependencies ++= Seq(
  "rhino" % "js" % "1.7R2"
)
