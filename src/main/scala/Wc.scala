package home

import model.Params
import model.WcOption.{Bytes, Characters, Lines, Words}

import java.io.File
import scala.io.{Codec, Source}
import scala.util.Using

object Wc extends App {

  private val arguments: Array[String] = Array("-lwcm", "/Users/victor/Documents/Work/CV_Morozov_Victor_DataArt.docx")
  private val codec = Codec.ISO8859
  var previousIsWhitespace = true //use var until figure out how not to

  (getParams _ andThen count andThen println) (arguments)

  //todo: count everything in one iteration
  def count(params: Params) =
    params.options.map {
      case Words => countWords(params.path.toFile)
      case Characters => countBytes(params.path.toFile) //don't know how to count characters, don't care
      case Lines => countLines(params.path.toFile)
      case Bytes => countBytes(params.path.toFile)
    }.mkString("  ") +
      f"  ${params.path.getFileName}"


  def countLines(file: File) = Using(Source.fromFile(file)(codec))(_.count(_ == '\n')).get

  def countBytes(file: File) = Using(Source.fromFile(file)(codec))(_.count(_ => true)).get

  def countWords(file: File) = Using(Source.fromFile(file)(codec))(_.count(isWordStart)).get

  def isWordStart(char: Char): Boolean =
    if (!char.isWhitespace && previousIsWhitespace) {
      previousIsWhitespace = false
      true
    } else if (char.isWhitespace) {
      previousIsWhitespace = true
      false
    } else false

  def getParams(args: Array[String]) = Utils.getParams(args) {
    case 'l' => Lines
    case 'w' => Words
    case 'c' => Bytes
    case 'm' => Characters
  }
}
