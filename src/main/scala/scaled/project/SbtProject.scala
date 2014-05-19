//
// Scaled SBT Project plugin - a Scaled extension for handling SBT projects
// http://github.com/scaled/sbt-project/blob/master/LICENSE

package scaled.project

import java.io.File
import scaled._

class SbtProject (root :File, metaSvc :MetaService, projectSvc :ProjectService)
    extends FileProject(root, metaSvc) {

  // TODO: things

  override protected def ignores = SbtProject.sbtIgnores
}

object SbtProject {

  // TODO: don't do things this way, determine the classes directory from SBT, etc.
  val sbtIgnores = FileProject.stockIgnores ++ Set("target")

  @Plugin(tag="project-finder")
  class FinderPlugin extends ProjectFinderPlugin("sbt", true, classOf[SbtProject]) {
    def checkRoot (root :File) :Int = {
      if (new File(root, "build.sbt").exists) 1
      else {
        val pdir = new File(root, "project")
        if (!pdir.isDirectory) -1
        else if (new File(pdir, "build.properties").exists ||
                 new File(pdir, "plugins.sbt").exists ||
                 new File(pdir, "Build.scala").exists) 1
        // TODO: look for any .scala file in a project directory? oh sbt...
        else -1
      }
    }
  }
}
