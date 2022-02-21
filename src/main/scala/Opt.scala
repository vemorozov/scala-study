package home


sealed trait Opt

object Opt {
  case object ShowHidden extends Opt

  case object FormatLong extends Opt
}

