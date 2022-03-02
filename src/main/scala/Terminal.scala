package home

import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.{PosixFileAttributes, PosixFilePermissions}

object Terminal {
  /*def main(args: Array[String]): Unit = {
    while (true) {
      val input = Console.in.readLine().trim
      if (input.isLsCommand) doLs(input)
      else print("Command " + input.firstWord + " not recognized")
    }
  }

  private def doLs(line: String): Unit = {
    val params = getParams(line)
    list(params)
  }

  private def list(params: Params): Unit = {
    val currentDir = new File(params.path)

    if (!currentDir.exists) {
      println("No such file")
      return
    }
    if (!currentDir.isDirectory) {
      println("Not a directory")
      return
    }

    var result = currentDir.listFiles()
    if (!params.options.contains("a")) {
      result = result.filter(file => !file.getName.startsWith("."))
    }

    printResult(result, params)
  }

  private def printResult(result: Array[File], params: Params): Unit = {
    if (params.options.contains("l")) {
      printResultLong(result)
    } else result.foreach(item => printf("%s    ", item.getName))
  }

  private def printResultLong(result: Array[File]): Unit = {
//    var nLinksW = 1
//    var ownerW = 1
//    var groupW = 1
//    var sizeW = 1
//    for (file <- result) {
//      val attributes = Files.readAttributes(file.toPath, classOf[PosixFileAttributes])
//      val nLinks = Files.getAttribute(file.toPath, "unix:nlink")
//
//      if (nLinksW < nLinks.toString.toInt.length) nLinksW = nLinks.toString.toInt.length
//      if (ownerW < attributes.owner().getName.length) ownerW = attributes.owner().getName.length
//      if (groupW < attributes.group().getName.length) groupW = attributes.group().getName.length
//      if (sizeW < attributes.size().toInt.length) sizeW = attributes.size().toInt.length
//    }

    for (file <- result) {
      val attributes = Files.readAttributes(file.toPath, classOf[PosixFileAttributes])
      val nLinks = Files.getAttribute(file.toPath, "unix:nlink")

      println(f"${attributes.permissionsString} $nLinks ${attributes.owner} ${attributes.group()} " +
        f"${attributes.size()} ${attributes.lastModifiedTime()} ${file.getName}")
    }
  }

  private def getParams(line: String): Params = {
    if (line.length == 2) {
      return Params()
    }
    val params = line.substring(3).split(" ")
    val path = params.find(p => !p.startsWith("-"))
    val options = params.filter(p => p.startsWith("-"))
                        .flatMap(s => s.substring(1))
                        .map(c => c.toString)
                        .toList
    if (path.isDefined) {
      Params(options, path.get)
    } else Params(options)
  }

  final implicit class StringImprovements(s: String) {
    def isLsCommand: Boolean = {
      s == "ls" || s.startsWith("ls ")
    }

    def firstWord: String = {
      if (s.contains(" ")) s.substring(0, s.indexOf(" "))
      else s
    }
  }

  implicit class PosixFileAttributesImprovements(attr: PosixFileAttributes) {
    def permissionsString: String = {
      PosixFilePermissions.toString(attr.permissions())
    }
  }

  implicit class IntImprovements(int: Int) {
    def length: Int = {
      var length = 0
      var remainder = int
      while (remainder != 0) {
        remainder = int / 10
        length += 1
      }
      length
    }
  }*/
}
