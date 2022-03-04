package home

import model.{Opt, Params}

import java.nio.file.Path

object Utils {
  /**
   *
   * @param args        command line arguments
   * @param mapFunction function to convert options presented as characters into dedicated [[model.Opt]]
   * @return
   */
  def getParams(args: Array[String])(mapFunction: Function[Char, Opt]): Params = {
    val pathSpecified = args.nonEmpty && !args.last.contains("-")
    val path = if (pathSpecified) Path.of(args.last) else Path.of(".")
    val options: Set[Opt] = (if (pathSpecified) args.init else args)
      .flatMap(arg => arg.replaceAll("-", ""))
      .map(mapFunction)
      .toSet
    Params(options, path)
  }
}
