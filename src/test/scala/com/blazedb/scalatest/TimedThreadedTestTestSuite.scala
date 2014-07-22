package com.blazedb.scalatest

/**
 * ThreadedTestTestSuite
 *
 * Created by sboesch@apple.com on 7/22/14
 */

import java.util.Date
import java.util.concurrent.Callable

import org.scalatest.FunSuite

class TimedThreadedTestTestSuite extends FunSuite with TimedThreadedTest {

  test("demonstrate fine grained synchronization using monitors on interned strings") {
    def takeSomeTime() = {
      Thread.sleep(200)
    }
    import scala.collection.JavaConverters._
    val sl = collection.mutable.ListBuffer[Int]()
    val jl = sl.asJava
    def NLoops = 20
    import collection.mutable
    val map = new mutable.HashMap[String, String]()

    val NItems = 20
    val testItems = Range(1, NItems + 1).map(_.toString)

    val frunners = Array.tabulate(NItems)(n => {
      val tname = s"FineGrainedRunner-$n"
      new TestRunner[Any](tname, new Callable[Any]() {
        override def call(): CallRet[Any] = {
          val start = new Date().getTime
          testItems(n).synchronized {
            takeSomeTime()
          }
          CallRet[Any](tname, None, duration(start))
        }
      })
    })
    val crunners = Array.tabulate(NItems)(n => {
      val tname = s"CoarseGrainedRunner-$n"
      new TestRunner[Any](tname, new Callable[Any]() {
        override def call(): CallRet[Any] = {
          val start = new Date().getTime
          testItems.synchronized {
            takeSomeTime()
          }
          CallRet(tname, None, duration(start))
        }
      })
    })
    val tests = Seq(("FineGrainedTests", frunners), ("CoarseGrainedTests", crunners))
    tests.foreach { case (tname, runners) => {
      threadedTest(tname, runners)
    }
    }
    log("HEY we are done!")
  }
}