package com.blazedb.scalatest

/**
 * ThreadedTest
 *
 * Created by javadba@gmail.com on 7/22/14
 */
import java.util.Date
import java.util.concurrent.{Callable, CyclicBarrier, Executors, Future}

import org.apache.log4j.Logger

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

trait TimedThreadedTest {

  val logger = Logger.getLogger(getClass.getName)

  type TestRunner[T] = (String, Callable[T])
//  type TimedRunner[T] = (TestRunner[T], Float)
  case class CallRet[T](testName: String, result: T, duration: Float)  {
    override def toString = s"TestResult for $testName: result=$result duration=$duration"
  }

  def timedTest[T](barrier: CyclicBarrier, testRunner: TestRunner[T]): CallRet[T] = {
    val testName = testRunner._1
    barrier.await
    val startTime = new Date().getTime
    val (name, result) : (String, T)  = (testRunner._1, testRunner._2.call)
    val lduration = duration(startTime)
//    debug(s"$testName completed: duration= $lduration")
    CallRet(name, result, lduration)
  }
  case class TTestResult[T](tname : String, callRets : ArrayBuffer[CallRet[T]] , lduration : Float)

  def threadedTest[T](testName: String, tests: Seq[TestRunner[T]]) : /* (String, ArrayBuffer[CallRet[T]], Float) */ TTestResult[T]  = {
    val nThreads = tests.length
    val threads = new ArrayBuffer[Thread](nThreads)
    val barrier = new CyclicBarrier(nThreads)
    val tpool = Executors.newFixedThreadPool(nThreads)

    val calls = mutable.ArrayBuffer[Future[CallRet[T]]]()
    val start = new Date().getTime
    for ((test, ix) <- tests zip Range(1, tests.size + 1)) {
      calls += tpool.submit(new Callable[CallRet[T]]() {

        override def call(): CallRet[T] = {
          val result = timedTest(barrier, test)
          result
        }
      })
    }
    val rets = calls.map(_.get)
    val lduration= duration(start)
    log(s"** Completed $testName duration=$lduration**")
    new TTestResult(testName, rets, lduration)
  }

  def log(msg: String) = {
    logger.info(s"$msg")
  }

  def debug(msg: String) = {
    logger.debug(s"$msg")
  }

  def duration(startTime: Long): Float = {
    (Math.floor((new Date().getTime - startTime) / 100) / 10.0).toFloat
  }


}
