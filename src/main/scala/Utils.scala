package home

import model.{Opt, Params}

import java.nio.file.Path
import scala.reflect.ClassTag

object Utils {
  /**
   *
   * @param args        command line arguments
   *
   * @param mapFunction function to convert options presented as characters into dedicated [[model.Opt]]
   *
   * @tparam T [[model.Opt]] implementation
   * @return
   */
  def getParams[T <: Opt : ClassTag](args: Array[String])(mapFunction: Function[Char, T]): Params[T] = {
    val pathSpecified = args.nonEmpty && !args.last.contains("-")
    val path = if (pathSpecified) Path.of(args.last) else Path.of(".")
    val options: Set[T] = (if (pathSpecified) args.init else args)
      .flatMap(arg => arg.replaceAll("-", ""))
      .map(mapFunction)
      .toSet
    Params[T](options, path)
  }
}
