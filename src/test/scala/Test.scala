package home

import org.scalatest.funspec.AnyFunSpec

object Test


class CalculatorSpec extends AnyFunSpec {

  describe("basic stuff") {
    it("should create empty leest") {
      assert(Leest() == Nil)
    }
    it("should create actual leest") {
      assert(Leest(0, 1, 2).isInstanceOf[Leest[Int]])
      assert(Leest(0, 1, 2).isInstanceOf[Leest[Int]])
    }
    it("should be readable") {
      assertResult("Leest(1, 2, 3)")(Leest(1, 2, 3).toString)
      assertResult("Leest(1)")(Leest(1).toString)
      assertResult("Leest()")(Nil.toString)
    }
    it("should calculate size") {
      assert(Leest(0, 1, 2, 3, 4, 5).size == 6)
      assert(Leest().size == 0)
      assert(Leest(0).size == 1)
    }
  }

  describe("retrieving elements") {
    val leest = Leest(0, 1, 2)
    it("should get by index") {
      assert(leest(1) == 1)
    }
  }

  describe("addition") {
    val leest = Leest(0, 1, 2)

    it("should prepend") {
      assert(5 :: leest == Leest(5, 0, 1, 2))
      assert(5 :: Nil == Leest(5))
      assert(Nil :: leest == Leest(Nil, 0, 1, 2))
    }

    it("should concat") {
      assert(Leest(3, 4) ::: leest == Leest(3, 4, 0, 1, 2))
      assert(Leest(1) ::: Nil == Leest(1))
      assert(Nil ::: Leest(1) == Leest(1))
      assert((Nil ::: Nil).isInstanceOf[Leest[Nothing]])
    }
  }

  describe("mapping") {
    val leest = Leest(0, 1, 2)
    it("should map") {
      assert(leest.map(v => v.toString) == Leest("0", "1", "2"))
    }

    it("should map Nil") {
      assert(Nil.map(_ => true).isInstanceOf[Leest[Nothing]])
    }

    it("should flatMap") {
      assert(leest.flatMap(v => v :: Leest(v + 10)) == Leest(0, 10, 1, 11, 2, 12))
    }

    it("should flatMap Nil") {
      assert(Nil.map(_ => Leest(true)) == Leest[Boolean]())
    }
  }

  describe("filtering") {
    val leest = Leest(0, 1, 2)
    it("should filter") {
      val goodCars: String => Boolean = {
        case "BMW" => true
        case _ => false
      }

      assert(leest.filter(i => i % 2 == 0) == Leest(0, 2))
      assert(Leest("Mercedes", "Audi", "BMW").filter(goodCars) == Leest("BMW"))
    }
  }

  describe("withFilter") {
    def self[A]: A => A = i => i

    val wf = Leest(0, 1, 2).withFilter(i => i % 2 == 0)

    it("inits") {
      assert(wf.isInstanceOf[WithFilter[Int]])
      assert(Nil.withFilter(_ => true).isInstanceOf[WithFilter[Nothing]])
    }
    it("maps") {
      assertResult(Leest())(Nil.withFilter(_ => true).map(self))
      assertResult(Leest(0, 2))(wf.map(i => i))
    }
    it("flatMaps") {
      assertResult(Leest(0, 1, 2))(Leest(Leest(0, 1), Leest(2), Leest(3, 4)).withFilter(i => i.head < 3).flatMap(self))
    }
    it("allows recursive withFilter") {
      assertResult(Leest(1))(Leest(0, 1, 2).withFilter(i => i < 2).withFilter(i => i > 0).map(self))
    }
    it("works with foreach") {
      var j = 0
      Leest(0, 1, 2).withFilter(i => i != 1).foreach(_ => j += 1)
      assert(j == 2)
    }
  }

  describe("reverse") {
    it("should revert") {
      assert(Leest(0, 1, 2).reverse == Leest(2, 1, 0))
    }
  }
}