import Dependencies._

val stackpackName = "autosync"

/*
 * General build setup
 *
 * NOTE: publishing the StackPack is done by the StackState CI/CD pipeline. Its configuration is included in this build setup
 *       but requires StackState artifactory credentials which are not available to StackPack contributors.
 */
lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "com.stackstate.stackpack",
      updateOptions := updateOptions.value.withGigahorse(false), // Work around for bug: https://github.com/sbt/sbt/issues/3570
      scalaVersion := "2.12.6",
        scalacOptions ++= Vector(
          "-target:jvm-1.8",
          "-encoding",
          "UTF-8",
          "-feature",
          "-language:implicitConversions",
          "-language:postfixOps",
          "-unchecked",
          "-deprecation",
          "-Xfatal-warnings",
          "-Xmax-classfile-name",
          "242",
          "-Yrangepos"
        ),
        javacOptions ++= Vector("-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-Xlint:deprecation", "-Werror"),
        resolvers ++= Vector("Stackstate Artifactory" at "https://artifactory.stackstate.io/artifactory/public", Resolver.mavenLocal),
        coursierUseSbtCredentials := true,
        // Publishing the StackPack is done by the StackState CI/CD pipeline.
        credentials += (if (sys.env.getOrElse("ARTIFACTORY_USERNAME", "").isEmpty) Credentials(Path.userHome / ".sbt" / "stackstate-artifactory-publish.credentials") else Credentials("Artifactory Realm","artifactory.stackstate.io",sys.env.getOrElse("ARTIFACTORY_USERNAME", ""),sys.env.getOrElse("ARTIFACTORY_PASSWORD", ""))),
        publishTo := Some("Artifactory Realm" at "https://artifactory.stackstate.io/artifactory/libs"),
        // disable publishing the main doc jar
        publishArtifact in (Compile, packageDoc) := false,
        // disable publishing the main sources jar
        publishArtifact in (Compile, packageSrc) := false
    )),
    name := stackpackName
  )

lazy val showVersion = taskKey[Unit]("Show version")

showVersion := {
  println(version.value)
}
