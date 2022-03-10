package home

sealed abstract class Leest[+A] {  // List being a trait is abusive to common sense
  def ::[B >: A](a: B): Leest[B]

  def apply(i: Int): A
}

object Leest {
  def apply[A](a: A*): Leest[A] = if (a.isEmpty) Nil else Cons(a.head, apply(a.tail: _*)) //what is : _* for?
}

case object Nil extends Leest[Nothing] {
  override def ::[B >: Nothing](a: B): Leest[B] = Cons(a, Nil)

  override def apply(i: Int): Nothing = throw new IndexOutOfBoundsException
}

case class Cons[+A](head: A, tail: Leest[A]) extends Leest[A] {
  override def ::[B >: A](a: B): Leest[B] = Cons(a, head :: tail)

  override def apply(i: Int): A = if (i == 0) head else tail(i - 1)
}