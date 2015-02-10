package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {


  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */



  
  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }
  
  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   * 
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   * 
   *   val s1 = singletonSet(1)
   * 
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   * 
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   * 
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
  }


  test("singletonSet(1) contains 1") {
    
    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3". 
     */
    new TestSets {
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersection is empty for disjoint sets"){
    new TestSets {
      val s = union(s1, s2)
      assert(!contains(s, 1), "Intersection 1")
      assert(!contains(s, 2), "Intersection 2")
      assert(!contains(s, 3), "Intersection 3")
    }
  }

  test("intersection of s1 and infinite set is s1"){
    new TestSets {
      val s = union(s1, (x:Int) => true)
      assert(contains(s, 1), "Intersection 1")
      assert(!contains(s, 2), "Intersection 2")
      assert(!contains(s, 3), "Intersection 3")
    }
  }

  test("difference of s and other disjoint set is original set"){
    new TestSets {
      val s = diff(union(s1, s2), s3)
      assert(contains(s, 1), "Diff 1")
      assert(contains(s, 2), "Diff 2")
      assert(!contains(s, 3), "Diff 3")
    }
  }

  test("difference of infinite and singleton set"){
    new TestSets {
      val s = diff((x: Int) => true, s3)
      assert(contains(s, 1), "Diff 1")
      assert(contains(s, 2), "Diff 2")
      assert(!contains(s, 3), "Diff 3")
    }
  }

  test("difference of empty and singleton set"){
    new TestSets {
      val s = diff((x: Int) => false, s3)
      assert(!contains(s, 1), "Diff 1")
      assert(!contains(s, 2), "Diff 2")
      assert(!contains(s, 3), "Diff 3")
    }
  }

  test("filtering empty set is still empty"){
    new TestSets {
      val s = filter((x: Int) => false, (x: Int) => (x % 2) == 0 )
      assert(!contains(s, 1), "Filter 1")
      assert(!contains(s, 2), "Filter 2")
      assert(!contains(s, 3), "Filter 3")
    }
  }

  test("filtering infinite set for even numbers"){
      val s = filter((x: Int) => true, (x: Int) => (x % 2) == 0 )
      assert(!contains(s, 1), "Filter 1")
      assert(contains(s, 2), "Filter 2")
      assert(!contains(s, 3), "Filter 3")
  }

  test("filtering s1 for even numbers"){
    new TestSets {
      val s = filter(s1, (x: Int) => (x % 2) == 0 )
      assert(!contains(s, 1), "Filter 1")
      assert(!contains(s, 2), "Filter 2")
    }
  }

  test("forall inputs in empty set, any predicate can be satisfied"){
      assert(forall((x: Int) => false, (x: Int) => true))
  }

  test("forall inputs in infinite set, even predicate not satisfied"){
    assert(!forall((x: Int) => true, (x: Int) => (x % 2) == 0))
  }

  test("forall inputs in even set meets even predicate"){
    assert(forall((x: Int) => (x % 2) == 0, (x: Int) => (x % 2) == 0))
  }

  test("forall inputs in even set meets divisible by 4 predicate"){
    assert(forall((x: Int) => (x % 2) == 0, (x: Int) => (x % 4) == 0))
  }

  test("exists inputs in even set a zero"){
    assert(exists((x: Int) => (x % 2) == 0, (x: Int) => x == 0))
  }

  test("doesn't exists a 1 in even set"){
    assert(!exists((x: Int) => (x % 2) == 0, (x: Int) => x == 1))
  }

  test("map works on an empty set"){
    val s = map((x: Int) => false, (x: Int) => x+1)
    assert(!contains(s,1))
  }

  test("map weven to odd set"){
    val s = map((x: Int) => (x % 2) == 0, (x: Int) => x+1)
    assert(contains(s,1))
    assert(!contains(s,2))
    assert(contains(s,3))
    assert(!contains(s,4))
  }
}
