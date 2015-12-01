import Common._
import sbt.Keys._

organization := "io.ddf"

name := "ddf"

version := ddfVersion

retrieveManaged := true

scalaVersion := theScalaVersion

scalacOptions := Seq("-unchecked", "-optimize", "-deprecation")

// Fork new JVMs for tests and set Java options for those
fork in Test := true

parallelExecution in ThisBuild := false

javaOptions in Test ++= Seq("-Xmx2g")

concurrentRestrictions in Global += Tags.limit(Tags.Test, 1)

conflictManager := ConflictManager.strict

commonSettings

libraryDependencies ++= Seq("com.univocity" % "univocity-parsers" % "1.5.5",
  "com.clearspring.analytics" % "stream" % "2.7.0" exclude("asm", "asm"),
  "org.scalatest" %% "scalatest" % "3.0.0-M7")

val com_adatao_unmanaged = Seq(
  "com.adatao.unmanaged.net.rforge" % "REngine" % "2.1.1.compiled",
  "com.adatao.unmanaged.net.rforge" % "Rserve" % "1.8.2.compiled"
)

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

resolvers ++= Seq("Adatao Mvnrepos Snapshots" at "https://raw.github.com/adatao/mvnrepos/master/snapshots",
  "Adatao Mvnrepos Releases" at "https://raw.github.com/adatao/mvnrepos/master/releases")

publishMavenStyle := true
