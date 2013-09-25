sbtPlugin := true

scalaVersion in ThisBuild := "2.10.2"

name := "sbt-requirejs"

organization := "com.gu"

libraryDependencies ++= Seq(
  "rhino" % "js" % "1.7R2"
)
