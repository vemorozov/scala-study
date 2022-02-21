package home

import java.nio.file.Path

final case class Params(options: Set[Opt.type], path: Path)
