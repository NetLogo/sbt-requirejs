# SBT RequireJS Plugin

This plugin provides a simple means of utilizing the RequireJS optimizer from within SBT.

Currently, this plugin targets **SBT 0.13**.

## Building

Simply run the `package` SBT command to build a new version of the plugin `.jar`.  Then, set your SBT project's `plugins.sbt` to reference/fetch the `.jar`.

## Usage

When the plugin is included in your build, the `RequireJS` object comes into scope.  Inside that object are the following:

<u><b>Tasks</b></u>

  * `runRequireJS`/`run-require-js`: Runs the optimizer
  * `cleanRequireJS`/`clean-require-js`: Deletes the output file, if it exists

<br><u><b>Settings</b></u>

  * `projectDir`: The **File** that represents the directory that is the root of your RequireJS project
  * `outFile`: The **File** that represents where you want the RequireJS output file to go
  * `shim`: The **Option[String]** of a path relative to `projectDir` of what RequireJS config file to use
  * `optimize`: The **Boolean** value that signifies whether or not you want your code run through an optimizer/minifier
    * If unspecified, this defaults to `true`

## Terms of Use

This code is released under the Apache 2 license, courtesy of The Guardian.  Please see [LICENSE.txt](LICENSE.txt) for further details.
