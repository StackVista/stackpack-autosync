import sbt.Keys._
import sbt._
import scala.sys.process._
import scala.io.Source
import sbt.Package._

object StackPackKeys {
  val packageStackPack = taskKey[Seq[File]]("Build and package custom code for the stackpack, for example python code that will deployed during provisioning.")
  val stackPackDir = settingKey[File]("Directory with the StackPack contents")
  val lastReleasedStackPackVersion = settingKey[String]("The StackPack version that is/was released. It is used as the basis for the actual version: by default this version will be suffixed with a branch name, only when the current git HEAD is at a tag with `<stackpack-name>-<version>` it will use this exact version.")
  val minSupportedStackStateVersion = settingKey[String]("The minimum version of StackState this StackPack supports. This is used to denote the first version of StackState in which this StackPack was supported.")
  val maxSupportedStackStateVersion = settingKey[Option[String]]("The maximum version of StackState this StackPack supports. This is used to denote the last version of StackState in which this StackPack was supported.")
}

object StackPack extends AutoPlugin {
  private val StackPackConf = "stackpack.conf"

  private val Version = "(\\d+.\\d+.)(\\d+)([^\\s]*)".r
  override def trigger = allRequirements

  val autoImport = StackPackKeys

  import autoImport._

  override def projectSettings = Vector(
    crossPaths := false, // Disable scala version in artifact name
    stackPackDir := baseDirectory.value / "src" / "main" / "stackpack",
    artifact in (Compile, packageBin) := (artifact in (Compile, packageBin)).value.withExtension("sts").withType("sts"),
    Compile / resourceGenerators += packageStackPack.taskValue,
    Compile / packageBin / packageOptions += Package.addSpecManifestAttributes("stackpack", "1.0.0", "com.stackstate"),
    libraryDependencies ++= List(
      Dependencies.stackPackSdk % "compile->compile; test->test",
      Dependencies.stackstateApiTesting % Test,
      Dependencies.mockito,
      Dependencies.scalaTest,
      Dependencies.scallop % Test
    ),
    packageStackPack := {
      val sourceDir = stackPackDir.value
      val targetDir = (Compile / resourceManaged).value
      val stackPackConf = sourceDir / StackPackConf

      // copy everything except the stackpack.conf file
      val mapping = ((stackPackDir.value ** "*") --- stackPackConf) pair Path.rebase(sourceDir, targetDir)
      val stackPackResources = mapping.map { case (sourceFile, targetFile) =>
        if (sourceFile.isFile) IO.copyFile(sourceFile, targetFile, true)
        else if (!targetFile.exists) IO.createDirectory(targetFile)

        targetFile
      }
      // copy stackpack.conf file and add version
      copyAndModifyConfFile(sourceDir, targetDir, version.value) +: stackPackResources
    },
    version := determineVersion(lastReleasedStackPackVersion.value)
  )

  def determineVersion(stackPackVersion: String): String = {
    val isTag = {
      val gitTags = ("git tag --points-at HEAD".!!).split("\n").toSet + sys.env.getOrElse("CI_COMMIT_TAG", "")
      gitTags.contains(s"$stackPackVersion")
    }

    if (isTag) stackPackVersion
    else {
      val gitlabBranch = sys.env.get("CI_COMMIT_REF_NAME")
      val branch = gitlabBranch.getOrElse(("git rev-parse --abbrev-ref HEAD".!!).trim)
      s"${incrementVersion(stackPackVersion)}-$branch-SNAPSHOT"
    }
  }

  def incrementVersion(stackPackVersion: String) = stackPackVersion match {
    case Version(majorMinor, patch, meta) => s"${majorMinor}${patch.toInt + 1}${meta}"
  }

  def copyAndModifyConfFile(sourceDir: File, targetDir: File, stackpackVersion: String) = {
    val confSource = Source.fromFile(sourceDir / StackPackConf)
    val confTarget = targetDir / StackPackConf
    try {
      val stackPack = confSource.getLines.mkString("\n")
      val stackPackWithVersion = s"""$stackPack\nversion = ${stackpackVersion}"""
      IO.write(confTarget, stackPackWithVersion)

      confTarget
    } finally {
      confSource.close()
    }
  }
}
