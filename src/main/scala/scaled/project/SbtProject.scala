//
// Scaled SBT Project plugin - a Scaled extension for handling SBT projects
// http://github.com/scaled/sbt-project/blob/master/LICENSE

package scaled.project

import java.nio.file.Path
import scaled._

class SbtProject (root :Path, metaSvc :MetaService, projectSvc :ProjectService)
    extends FileProject(root, metaSvc) {

  // TODO: things

  override protected def ignores = SbtProject.sbtIgnores
}

object SbtProject {

  // TODO: don't do things this way, determine the classes directory from SBT, etc.
  val sbtIgnores = FileProject.stockIgnores ++ Set("target")

  @Plugin(tag="project-finder")
  class FinderPlugin extends ProjectFinderPlugin("sbt", true, classOf[SbtProject]) {
    def checkRoot (root :Path) :Int = {
      if (exists(root, "build.sbt")) 1
      else if (seeProjectBits(root)) 1
      // TODO: look for any .scala file in a project directory? oh sbt...
      else -1
    }

    private def seeProjectBits (root :Path) = exists(root, "project") && {
      val pdir = root.resolve("project")
      exists(pdir, "build.properties") || exists(pdir, "plugins.sbt") || exists(pdir, "Build.scala")
    }
  }
}
