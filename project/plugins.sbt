addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.1")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.2")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.13")
addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.6.3")
addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.3.1")
addSbtPlugin("com.lightbend.paradox" % "sbt-paradox" % "0.7.0")
addSbtPlugin("io.github.jonas" % "sbt-paradox-material-theme" % "0.6.0")

resolvers += Resolver.jcenterRepo
addSbtPlugin("net.aichler" % "sbt-jupiter-interface" % "0.8.3")

// This does not work well for unknown error. Use IntelliJ code coverage
// tool for
//addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.2.0")
