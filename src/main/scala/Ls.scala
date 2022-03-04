package home

import model.LsOption.{FormatLong, ShowHidden}
import model.{Opt, Params}

import java.nio.file.{Files, Path}
import java.util.stream.Stream
import scala.jdk.CollectionConverters.IteratorHasAsScala

object Ls {
  def main(args: Array[String]): Unit = println {
    (getParams _ andThen list andThen format) (args)
  }

  private def format(files: List[Path]) = files.map(f => f.toFile.getName)

  private def list(params: Params) = filter(Files.list(params.path), params.options)
    .iterator().asScala.toList

  private def filter(files: Stream[Path], options: Set[Opt]) =
    if (options.contains(ShowHidden)) files
    else files.filter(f => !f.toFile.isHidden)

  private def getParams(args: Array[String]) = {
    Utils.getParams(args) {
      case 'a' => ShowHidden
      case 'l' => FormatLong
    }
  }
}
