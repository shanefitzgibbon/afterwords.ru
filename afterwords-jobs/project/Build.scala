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
      "securesocial" % "securesocial_2.9.1" % "2.0.7"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      javacOptions in Compile ++= Seq("-version"),  
      resolvers ++= Seq(
          "SecureSocial Repository" at "http://securesocial.ws/repository/releases/",
          "sonatype" at "https://oss.sonatype.org/content/repositories/releases/")
    )

}
