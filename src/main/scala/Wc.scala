package home

import WcOption.{Bytes, Characters, Lines, Words}

import java.nio.file.Path
import scala.io.Source
import scala.util.Using

object Wc extends App {


  println(count(getOpts(Array("-l", "-wc", "/Users/victor/Documents/Work/CV_Morozov_Victor_DataArt.docx"))))

  def count(params: Params[WcOption]) = {
    params.options.map {
      case WcOption.Words => "w" + countWords(params.path)
      case WcOption.Characters => "c" + countCharacters(params.path)
      case WcOption.Lines => "l" + countLines(params.path)
      case WcOption.Bytes => "b" + countCharacters(params.path)
    }.mkString("  ")
  }

  def countWords(path: Path) = {
    Using(Source.fromFile(path.toFile, "ISO-8859-1" /*todo: extract codec*/)) { reader =>
      reader.count(char => char.isWhitespace) //fixMe: fix multi-space counting
    }.get
  }

  def countLines(path: Path) = {
    //fixMe: solve OutOfMemory for large lines
    Using(Source.fromFile(path.toFile, "ISO-8859-1")) { reader => reader.getLines().size }.get
  }

  //todo: is it actually countBytes?
  def countCharacters(path: Path) = {
    Using(Source.fromFile(path.toFile, "ISO-8859-1")) { reader => reader.count(_ => true) }.get
  }

  def getOpts(args: Array[String]) = {
    val pathSpecified = args.nonEmpty && !args.last.contains("-")
    val path = if (pathSpecified) Path.of(args.last) else Path.of(".")
    val options: Set[WcOption] = (if (pathSpecified) args.init else args)
      .flatMap(arg => arg.replaceAll("-", ""))
      .map {
        case 'l' => Lines
        case 'w' => Words
        case 'c' => Bytes
        case 'm' => Characters
      }.toSet
    Params[WcOption](options, path)
  }
}
