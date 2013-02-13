import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "afterwords-jobs"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "org.scalatest" %% "scalatest" % "1.8" % "test",  
      "org.squeryl" %% "squeryl" % "0.9.5-2",
      "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
      "securesocial" % "securesocial_2.9.1" % "2.0.7"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      resolvers += Resolver.url("SecureSocial Repository", url("http://securesocial.ws/repository/releases/"))(Resolver.ivyStylePatterns)
    )

}
