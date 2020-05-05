//noinspection SpellCheckingInspection
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.1")
//noinspection SpellCheckingInspection,SpellCheckingInspection
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.2")
//noinspection SpellCheckingInspection
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.13")
//noinspection SpellCheckingInspection
addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.6.3")
addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.3.1")
//noinspection SpellCheckingInspection
addSbtPlugin("com.lightbend.paradox" % "sbt-paradox" % "0.7.0")
addSbtPlugin("io.github.jonas" % "sbt-paradox-material-theme" % "0.6.0")
//noinspection SpellCheckingInspection
addSbtPlugin("com.lightbend.sbt" % "sbt-java-formatter" % "0.5.1")

resolvers += Resolver.jcenterRepo
//noinspection SpellCheckingInspection
addSbtPlugin("net.aichler" % "sbt-jupiter-interface" % "0.8.3")

// This does not work well for unknown error. Use IntelliJ code coverage
// tool for
//addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.2.0")
