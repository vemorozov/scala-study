package home

import LsOption.{FormatLong, ShowHidden}

import java.nio.file.{Files, Path}
import java.util.stream.Stream
import scala.jdk.CollectionConverters.IteratorHasAsScala

object Ls {
  def main(args: Array[String]): Unit = println {
    (getParams _ andThen list andThen format) (args)
  }

  private def format(files: List[Path]) = files.map(f => f.toFile.getName)

  private def list(params: Params[LsOption]) = filter(Files.list(params.path), params.options)
    .iterator().asScala.toList

  private def filter(files: Stream[Path], options: Set[LsOption]) =
    if (options.contains(ShowHidden)) files
    else files.filter(f => !f.toFile.isHidden)

  private def getParams(args: Array[String]) = {
    Utils.getParams[LsOption](args) {
      case 'a' => ShowHidden
      case 'l' => FormatLong
    }
  }
}
