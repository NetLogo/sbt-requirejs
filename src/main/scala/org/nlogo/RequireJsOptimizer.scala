package org.nlogo

import sbt._
import io.Source
import org.mozilla.javascript.tools.shell.{ Global, ShellContextFactory, Main => Rhino }
import java.util

// to turn off optimization use optimize=Some("none") - yeah, I know
case class RequireJsConfig(out: File, mainConfig: Option[File], optimize: Option[String])

object RequireJsOptimizer {

  private val rjsFile = {
    val dir = IO.createTemporaryDirectory
    dir.deleteOnExit()
    val rjsFile = dir / "r.js"
    val rjs = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("r.js")).mkString
    IO.write(rjsFile, rjs)
    rjsFile
  }

  def optimize(config: RequireJsConfig, baseDir: File)(implicit log: Logger): Unit =
    time {

      val relativeOut = config.out.relativeTo(baseDir) getOrElse (throw new IllegalArgumentException("Output location must be within project base directory."))

      val outArr        = Array(s"out=$relativeOut")
      val optimizeArr   = config.optimize.toArray map (x => s"optimize=$x")
      val configFileArr = config.mainConfig.toArray map (_.getAbsolutePath)

      log.info("Running requirejs optimization with config file at " + (configFileArr.headOption getOrElse "<none>"))

      //if we do not do this then the script is not executed again
      //I found this out by trial and error
      Rhino.shellContextFactory = new ShellContextFactory()
      Rhino.global = new Global()
      clearFileList()

      val args = Array(rjsFile.getAbsolutePath, "-o") ++ configFileArr ++ outArr ++ optimizeArr
      log.info(s"""Running Rhino command: ${args.mkString(" ")}""")

      val result = Rhino.exec(args)

      //have not actually been able to get this to return anything other than 0
      //even when I know there are errors.
      if (result != 0) {
        System.exit(result)
      }

    }

  private def clearFileList() {
    //due to the way we are calling this, it adds a null to the list each time.
    //this is the only way I found to clear the list
    val fileList = classOf[Rhino].getDeclaredField("fileList")
    fileList.setAccessible(true)
    fileList.set(null, new util.ArrayList[String]())
  }

  private def time[A](block: => A)(implicit log: Logger): A = {
    val start = System.currentTimeMillis
    val result = block
    log.info("RequireJs optimization took " + (System.currentTimeMillis() - start) + " ms")
    result
  }

}
