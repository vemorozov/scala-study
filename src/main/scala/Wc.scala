package home

import model.WcOption.{Bytes, Characters, Lines, Words}
import model.{Params, WcOption}

import java.nio.file.Path
import scala.io.Source
import scala.util.Using

object Wc extends App {

  private val args: Array[String] = Array("-l", "-wc", "/Users/victor/Documents/Work/CV_Morozov_Victor_DataArt.docx")

  println(count(getParams(args)))

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

  def getParams(args: Array[String]) = {
    Utils.getParams[WcOption](args) {
      case 'l' => Lines
      case 'w' => Words
      case 'c' => Bytes
      case 'm' => Characters
    }
  }

  case class CountResult(lines: Int = null, words: Int = null, characters: Int = null, bytes: Int = null)
}
