package home
package model

object WcOption {
  case object Words extends WcOption
  case object Characters extends WcOption
  case object Lines extends WcOption
  case object Bytes extends WcOption
}

sealed trait WcOption extends Opt
