Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val root = (project in file("."))
  .settings(
      name         := "jscdg",
      organization := "com.github.tnakamot",
      description  := "JSON Schema Code and Document Generator",
      scalaVersion := "2.12.10",
      crossPaths   := false, // Do not use Scala version in artifacts.
      libraryDependencies += "net.sourceforge.argparse4j" % "argparse4j" % "0.8.1",
      libraryDependencies += "com.googlecode.json-simple" % "json-simple" % "1.1.1",
      libraryDependencies += "org.apache.commons" % "commons-text" % "1.8",
  )