import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "afterwords-jobs"
    val appVersion      = "0.1-SNAPSHOT"

    val appDependencies = Seq(
      "org.scalatest" %% "scalatest" % "1.8" % "test",
      "org.mongodb" %% "casbah" % "2.5.0",
      "com.novus" %% "salat" % "1.9.1",
      "org.scalaj" %% "scalaj-collection" % "1.2",
      "securesocial" %% "securesocial" % "2.0.12"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      javacOptions in Compile ++= Seq("-version"),
      resolvers += Resolver.url("sbt-plugin-snapshots", url("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)

    )

}
