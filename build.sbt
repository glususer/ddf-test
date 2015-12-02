
organization := "io.ddf"

name := "ddf"

version := "1.0"

retrieveManaged := true

scalaVersion := "2.10.4"

scalacOptions := Seq("-unchecked", "-optimize", "-deprecation")

// Fork new JVMs for tests and set Java options for those
fork in Test := true

parallelExecution in ThisBuild := false

javaOptions in Test ++= Seq("-Xmx2g")

concurrentRestrictions in Global += Tags.limit(Tags.Test, 1)

conflictManager := ConflictManager.strict

libraryDependencies ++= Seq( "io.ddf" %% "ddf_core" % "1.5.0-SNAPSHOT",
  "com.univocity" % "univocity-parsers" % "1.5.5",
  "com.clearspring.analytics" % "stream" % "2.7.0" exclude("asm", "asm"),
  "org.scalatest" %% "scalatest" % "3.0.0-M7"
)

val com_adatao_unmanaged = Seq(
  "com.adatao.unmanaged.net.rforge" % "REngine" % "2.1.1.compiled",
  "com.adatao.unmanaged.net.rforge" % "Rserve" % "1.8.2.compiled"
)

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

resolvers ++= Seq("Adatao Mvnrepos Snapshots" at "https://raw.github.com/adatao/mvnrepos/master/snapshots",
  "Adatao Mvnrepos Releases" at "https://raw.github.com/adatao/mvnrepos/master/releases")

publishMavenStyle := true
