package home.model

import java.nio.file.Path

final case class Params[T <: Opt](options: Set[T], path: Path)
