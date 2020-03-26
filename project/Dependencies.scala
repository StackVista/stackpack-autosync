import sbt._
object Dependencies {
  lazy val stackPackSdk = "com.stackstate.stackpack" %% "sdk" % "0.23.1" classifier "tests" classifier ""
  lazy val stackstateApiTesting = "com.stackstate.api" %% "testing" % "1.0.0"

  // Test dependencies
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % Test
  lazy val mockito = "org.mockito" % "mockito-all" % "1.10.19" % Test
  lazy val scallop = "org.rogach" %% "scallop" % "3.1.3" // Simple Scala command line parsing
}
