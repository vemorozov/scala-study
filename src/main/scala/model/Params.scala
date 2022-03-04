package home
package model

import java.nio.file.Path

final case class Params(options: Set[Opt], path: Path)
