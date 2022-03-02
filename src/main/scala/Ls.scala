package home

import LsOption.{FormatLong, ShowHidden}

import java.nio.file.{Files, Path}
import java.util.stream.Stream
import scala.jdk.CollectionConverters.IteratorHasAsScala

object Ls {
  def main(args: Array[String]): Unit = println {
    (getParams _ andThen list andThen format) (args.toList)
  }

  private def format(files: List[Path]) = files.map(f => f.toFile.getName)

  private def list(params: Params[LsOption]) = filter(Files.list(params.path), params.options)
    .iterator().asScala.toList

  private def filter(files: Stream[Path], options: Set[LsOption]) =
    if (options.contains(ShowHidden)) files
    else files.filter(f => !f.toFile.isHidden)

  private def getParams(args: List[String]) = {
    val pathSpecified = args.nonEmpty && !args.last.contains("-")
    val path = if (pathSpecified) Path.of(args.last) else Path.of(".")
    val options: Set[LsOption] = (if (pathSpecified) args.init else args).flatMap {
      case "-la" | "-al" => Set(ShowHidden, FormatLong)
      case "-a" => Set(ShowHidden) //todo remove wrapping if possible
      case "-l" => Set(FormatLong)
      case _ => Set.empty
    }.toSet
    Params[LsOption](options, path)
  }
}
