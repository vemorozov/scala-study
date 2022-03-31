package home

import scala.annotation.tailrec

sealed trait Leest[+A] {
  val size: Int

  def apply(i: Int): A

  def head: A

  def tail: Leest[A]

  def filter(f: A => Boolean): Leest[A]

  def withFilter(f: A => Boolean): WithFilter[A]

  def map[B](f: A => B): Leest[B]

  def flatMap[B](f: A => Leest[B]): Leest[B]

  def ::[B >: A](a: B): Leest[B]

  def :::[B >: A](other: Leest[B]): Leest[B]

  def isEmpty: Boolean

  def nonEmpty: Boolean = !isEmpty

  def reverse: Leest[A]
}

object Leest {
  def apply[A](a: A*): Leest[A] =
    a match {
      case a if a.isEmpty => Nil
      case _ => Cons(a.head, Leest(a.tail: _*))
    }
}

case object Nil extends Leest[Nothing] {
  override val size: Int = 0

  override def filter(f: Nothing => Boolean): Leest[Nothing] = Nil

  override def ::[B >: Nothing](b: B): Leest[B] = Cons(b, Nil)

  override def :::[B](other: Leest[B]): Leest[B] = other

  override def apply(i: Int): Nothing = throw new IndexOutOfBoundsException

  override def isEmpty: Boolean = true

  override def reverse: Leest[Nothing] = this

  override def tail: Leest[Nothing] = throw new UnsupportedOperationException("tail of empty leest")

  override def head: Nothing = throw new NoSuchElementException("head of empty leest")

  override def map[B](f: Nothing => B): Leest[B] = Leest[B]()

  override def flatMap[B](f: Nothing => Leest[B]): Leest[B] = Leest[B]()

  override def withFilter(f: Nothing => Boolean): WithFilter[Nothing] = new WithFilter(this, f)

  override def toString = "Leest()"
}

final case class Cons[+A](head: A, tail: Leest[A]) extends Leest[A] {
  override val size: Int = tail.size + 1

  override def :::[B >: A](other: Leest[B]): Leest[B] =
    other match {
      case Nil => this
      case _ =>
        val reverse = other.reverse
        reverse.tail ::: reverse.head :: this
    }

  override def ::[B >: A](a: B): Leest[B] = Cons(a, Cons(head, tail))

  override def apply(i: Int): A = i match {
    case 0 => head
    case _ => tail(i - 1)
  }

  override def filter(f: A => Boolean): Leest[A] = head match {
    case head if f(head) => Cons(head, tail.filter(f))
    case _ => tail.filter(f)
  }

  override def isEmpty: Boolean = false

  override def reverse: Leest[A] = {
    @tailrec
    def iter(acc: Leest[A], rest: Leest[A]): Leest[A] = {
      if (rest.isEmpty) acc else rest match {
        case x Cons xs => iter(Cons(x, acc), xs)
        case Nil => acc
      }
    }

    iter(Nil, this)
  }

  override def map[B](f: A => B): Leest[B] = Cons(f(head), tail.map(f))

  override def flatMap[B](f: A => Leest[B]): Leest[B] = f(head) ::: tail.flatMap(f)

  override def withFilter(f: A => Boolean): WithFilter[A] = new WithFilter[A](this, f)

  override def toString: String = "Leest(" + head + tailToString

  private def innerToString: String = ", " + head + tailToString

  private def tailToString = tail match {
    case Nil => ")"
    case t: Cons[A] => t.innerToString
  }
}

class WithFilter[+A](content: Leest[A], filter: A => Boolean) {
  //todo: Optimize(probably remove recursion)
  def map[B](mapFunction: A => B): Leest[B] = content match {
    case Nil => Nil
    case _ =>
      val mappedTail = content.tail.withFilter(filter).map(mapFunction)
      content.head match {
        case h if filter.apply(h) => mapFunction.apply(h) :: mappedTail
        case _ => mappedTail
      }
  }

  def flatMap[B](mapFunction: A => Leest[B]): Leest[B] = content match {
    case Nil => Nil
    case _ =>
      val mappedTail = content.tail.withFilter(filter).flatMap(mapFunction)
      content.head match {
        case h if filter.apply(h) => mapFunction.apply(h) ::: mappedTail
        case _ => mappedTail
      }
  }

  def foreach(f: A => Unit): Unit = content match {
    case Nil => ()
    case _ =>
      if (filter.apply(content.head)) f.apply(content.head)
      content.tail.withFilter(filter).foreach(f)
  }

  def withFilter(filter2: A => Boolean) = new WithFilter(content, (a: A) => filter(a) && filter2(a))
}