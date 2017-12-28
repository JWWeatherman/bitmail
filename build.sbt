import play.sbt.PlayImport.PlayKeys.playRunHooks

name := """bitmail"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies += jdbc
libraryDependencies += cache
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test
libraryDependencies += specs2 % Test
libraryDependencies += "org.reactivemongo" %% "play2-reactivemongo" % "0.12.5-play25"
libraryDependencies += "com.codeborne" % "phantomjsdriver" % "1.2.1"
libraryDependencies += "fr.acinq" % "bitcoin-lib_2.11" % "0.9.13"

playRunHooks += baseDirectory.map(Webpack.apply).value

routesGenerator := InjectedRoutesGenerator

excludeFilter in (Assets, JshintKeys.jshint) := "*.js"

watchSources ~= { (ws: Seq[File]) =>
    ws filterNot { path =>
        path.getName.endsWith(".js") || path.getName == ("build")
    }
}

pipelineStages := Seq(digest, gzip)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
