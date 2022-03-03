package home
package model

sealed trait LsOption extends Opt

object LsOption {
  case object ShowHidden extends LsOption

  case object FormatLong extends LsOption
}