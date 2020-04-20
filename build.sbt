/*
 * Copyright 2020 Takashi Nakamoto.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.io.FileUtils
import java.nio.file.{Path, Paths}
import scala.sys.process._
import ReleaseTransformations._
import sbt.nio.file.FileTreeView

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val buildReleasePackage = taskKey[Unit]("Task to build a release package.")
lazy val testDoxygenOutput = taskKey[Unit]("Generate doxygen outputs and convert the doxygen to PDF.")

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
    name         := "jscdg",
    organization := "com.github.tnakamot",
    description  := "JSON Schema Code and Document Generator",
    scalaVersion := "2.12.10",
    crossPaths   := false, // Do not use Scala version in artifacts.
    libraryDependencies += "net.sourceforge.argparse4j" % "argparse4j" % "0.8.1",
    libraryDependencies += "com.googlecode.json-simple" % "json-simple" % "1.1.1",
    libraryDependencies += "org.apache.commons" % "commons-text" % "1.8",

    // Packaging as universal plugin.
    mainClass in Compile := Some("com.github.tnakamot.jscdg.CLIMain"),
    discoveredMainClasses in Compile := Seq(),

    buildReleasePackage := {
      println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
      println("==============================================================")
      println("")
      println("Please add the release note and upload " + (Universal / packageBin).value.toString + " to")
      println("  https://github.com/tnakamot/jscdg/releases/new?tag=v" + version.value)
      println("")
      println("==============================================================")
      println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    },

    // Customized release process
    releaseCrossBuild := false,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      releaseStepTask(buildReleasePackage),
      setNextVersion,
      commitNextVersion,
      pushChanges,
    ),

    testDoxygenOutput := (Def.taskDyn {
      val validJSONSchemaGLob: Glob = (Test / resourceDirectory).value.toGlob / "valid" / "*.json"
      val validJSONSchemaFiles: Seq[Path] = FileTreeView.default.list(validJSONSchemaGLob).map(_._1)
      val outputDir = streams.value.cacheDirectory
      FileUtils.cleanDirectory(outputDir)
      val subCommand = "doxygen"
      val outputExt = "dox"
      val cmdLine = Seq(
        subCommand,
        "--output", outputDir.toString,
        "--extension", outputExt,
      ) ++ validJSONSchemaFiles.map(_.toString)
      val cmdLineStr = " " + cmdLine.mkString(" ")
      val cmd = " " + subCommand + " --help"

      Def.task {
        (Compile / run).toTask(cmdLineStr).value
        val outputFilesGlob: Glob = outputDir.toGlob / ("*." + outputExt)
        val outputFiles: Seq[Path] = FileTreeView.default.list(outputFilesGlob).map(_._1)

        val doxygenSourceFile = outputDir / "main.dox"
        val doxygenSourceContents =
          """
            |/*!
            |\page Introduction
            |\section json_schema JSON Schemas
            |""".stripMargin +
            outputFiles.map("\\includedoc " + _.getFileName.toString).mkString("\n") + "\n*/\n"
        IO.write(doxygenSourceFile, doxygenSourceContents)

        FileUtils.copyFile(
          (Test / resourceDirectory).value / "doxyconf",
          outputDir / "doxyconf")

        Process("doxygen" :: "doxyconf" :: Nil, outputDir) !

        Process("make", outputDir / "latex") !
      }
    }).value
  )