package com.gu

import java.io.File
import sbt._
import sbt.Keys._

object Plugin extends Plugin {

  object RequireJS {

    val runRequireJS   = TaskKey[Seq[File]]("require-js",  "Run the RequireJS optimizer")
    val cleanRequireJS = TaskKey[Unit]("clean-require-js", "Clean the RequireJS output directories")

    val projectDir = SettingKey[File](          "require-js-project-dir",  "The location of the javascript you want to optimize")
    val outFile    = SettingKey[File](          "require-js-out",          "The combined output file")
    val shim       = SettingKey[Option[String]]("require-js-shim",         "The path to the shim/config file for RequireJS to use (relative to the project dir)")
    val optimize   = SettingKey[Boolean](       "require-js-optimize",     "Whether or not to optimize files or not")

    val settings = Seq(
      runRequireJS   <<= compiler,
      cleanRequireJS <<= cleanUp,
      optimize        := true
    )

    def cleanUp = (outFile).map[Unit]{ _.delete() }

    def compiler = (optimize, projectDir, outFile, shim, streams, baseDirectory) map {
      (optimize, projectDir, out, shim, s, baseDir) =>

        implicit val log = s.log

        val optimizeOpt = if (optimize) None else Some("none")
        val config      = RequireJsConfig(out, shim map (s => (projectDir / s)), optimizeOpt)

        if (needsRecompile(projectDir, out)) {
          log.info("Optimizing javascript files")
          out.delete()
          RequireJsOptimizer.optimize(config, baseDir)
        } else {
          log.info("Skipping javascript file optimization")
        }

        Seq(out)

    }

    private def needsRecompile(projectRoot: File, out: File): Boolean = {
      val jsFiles = (projectRoot ** "*.js").get
      jsFiles exists (file => file.isFile && (file newerThan out))
    }

  }

}

