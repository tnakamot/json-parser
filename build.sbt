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

import ReleaseTransformations._

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val requestUploadTask = taskKey[Unit]("Task to ask the release manager to uplaod the package.")

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
      mainClass in Compile := Some("org.github.tnakamot.jscdg.CLIMain"),
      discoveredMainClasses in Compile := Seq(),

      requestUploadTask := {
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
          releaseStepCommand("universal:packageBin"),
          releaseStepTask(requestUploadTask),
          setNextVersion,
          commitNextVersion,
          pushChanges,

      )
  )