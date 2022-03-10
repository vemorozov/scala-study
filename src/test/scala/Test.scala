package home

import org.scalatest.funspec.AnyFunSpec

object Test


class CalculatorSpec extends AnyFunSpec {
  private val leest = Leest(0, 1, 2)

  describe("retrieving elements") {
    it("should get by index") {
      assert(leest(1) == 1)
    }
  }

  describe("addition") {
    it("should prepend") {
      assert(5 :: leest == Leest(5, 0, 1, 2))
    }
    it("should append") {
      assert(leest :: 5 :: Nil == Leest(0, 1, 2, 5))
    }
  }
}