package funsets

object Main extends App {
  import FunSets._
  println(contains(singletonSet(1), 1))

  Set even = (x: Int) => (x % 2) == 0
}
