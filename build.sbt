/*
 *  Copyright (C) 2020 Takashi Nakamoto <nyakamoto@gmail.com>.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import xerial.sbt.Sonatype._
import ReleaseTransformations._

Global / onChangedBuildSource := ReloadOnSourceChanges
resolvers += Resolver.jcenterRepo

ParadoxMaterialThemePlugin.paradoxMaterialThemeSettings(Paradox)
Paradox / paradoxMaterialTheme := {
  ParadoxMaterialTheme()
    .withColor("blue-grey", "indigo")
    .withLogo("assets/images/json_logo.png")
    .withFavicon("assets/images/json_logo.png")
    .withCopyright("Copyright (C) 2020 Takashi Nakamoto")
    .withSocial(uri("https://github.com/tnakamot"))
    .withLanguage(java.util.Locale.ENGLISH)
    .withSearch()
}

lazy val root = (project in file("."))
  .enablePlugins(GhpagesPlugin)
  .enablePlugins(ParadoxPlugin)
  .enablePlugins(ParadoxSitePlugin)
  .enablePlugins(ParadoxMaterialThemePlugin)
  .settings(
    name         := "json-parser",
    organization := "com.github.tnakamot",
    description  := "An implementation of Java JSON Parser",
    scalaVersion := "2.12.10",
    crossPaths   := false, // Do not use Scala version in artifacts.

    libraryDependencies ++= Seq(
      //noinspection SpellCheckingInspection,Annotator
      "org.apache.commons" % "commons-text" % "1.8",

      //noinspection SpellCheckingInspection,Annotator
      "org.dmfs" % "rfc3986-uri" % "0.8.1",

      //noinspection SpellCheckingInspection,Annotator
      "org.jetbrains" % "annotations" % "16.0.1",

      //noinspection SpellCheckingInspection,Annotator
      "net.aichler" % "jupiter-interface" % JupiterKeys.jupiterVersion.value % Test,

      //noinspection SpellCheckingInspection,Annotator
      "org.junit.jupiter" % "junit-jupiter-params" % "5.1.1" % Test,
    ),

    // For sbt-sonatype plugin to publish this package to Maven Central.
    publishTo := sonatypePublishToBundle.value,
    sonatypeProfileName := "com.github.tnakamot",
    publishMavenStyle := true,
    licenses     += ("GPL-3.0", url("https://www.gnu.org/licenses/gpl-3.0.en.html")),
    sonatypeProjectHosting := Some(GitHubHosting("tnakamot", "json-parser", "nyakamoto@gmail.com")),
    developers := List(
      Developer(
        id    = "nyakamoto",
        name  = "Takashi Nakamoto",
        email = "nyakamoto@gmail.com",
        url   = url("https://github.com/tnakamot"),
      )
    ),
    homepage := Some(url("https://tnakamot.github.io/json-parser/")),

    // Paradox setting  to generate web page.
    paradox / sourceDirectory := sourceDirectory.value / "paradox",
    paradoxProperties ++= Map(
    "extref.javadoc.base_url" -> "javadoc/com/github/tnakamot/json/%s"
    ),

    // Link to standard Java library in Javadoc.
    (doc / javacOptions) ++= Seq("-link", "https://docs.oracle.com/en/java/javase/11/docs/api"),

    // GitHub pages setting
    git.remoteRepo := "git@github.com:tnakamot/json-parser.git",
    siteSubdirName in Compile := "javadoc",
    addMappingsToSiteDir(mappings in (Compile, packageDoc), siteSubdirName in Compile),

    // Customized release process
    // Just run "sbt release".
    releaseCrossBuild := false,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      releaseStepTask(ghpagesPushSite),
      commitReleaseVersion,
      tagRelease,
      releaseStepCommandAndRemaining("publishSigned"),
      releaseStepCommand("sonatypeBundleRelease"),
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  )