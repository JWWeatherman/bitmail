import play.sbt.PlayImport.PlayKeys.playRunHooks

name := """bitmail"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
    .enablePlugins(PlayScala)
    .settings(
        watchSources ++= (baseDirectory.value / "public/ui" ** "*").get
    )

scalaVersion := "2.11.11"

libraryDependencies += jdbc
libraryDependencies += cache
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test
libraryDependencies += specs2 % Test
libraryDependencies += "com.codeborne" % "phantomjsdriver" % "1.2.1"
libraryDependencies += "fr.acinq" % "bitcoin-lib_2.11" % "0.9.13"
libraryDependencies += "com.typesafe.play" %% "play-mailer" % "6.0.1"
libraryDependencies += "com.typesafe.play" %% "play-mailer-guice" % "6.0.1"
libraryDependencies += "org.bitcoinj" % "bitcoinj-core" % "0.14.5"
libraryDependencies += "com.sendgrid" % "sendgrid-java" % "4.1.2"
libraryDependencies += "com.lihaoyi" %% "scalatags" % "0.6.7"
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.2.1"

routesGenerator := InjectedRoutesGenerator

pipelineStages := Seq(digest, gzip)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
