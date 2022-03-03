package home

import model.{Opt, Params}

import java.nio.file.Path

object Utils {
  def getParams[T <: Opt](args: Array[String])(function: Function[Char, T]): Params[T] = {
    val pathSpecified = args.nonEmpty && !args.last.contains("-")
    val path = if (pathSpecified) Path.of(args.last) else Path.of(".")
    val options: Set[T] = (if (pathSpecified) args.init else args)
      .flatMap(arg => arg.replaceAll("-", ""))
      .map(function)
      .toSet
    model.Params[T](options, path)
  }
}
