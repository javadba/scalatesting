package com.blazedb.scalatest

import com.google.common.util.concurrent.AtomicDouble

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}


/**
 * PageRank
 *
 */
object PageRank {

  import collection.mutable
//  case class Page(id: Int, var rank: Double = 1.0, val newRank: AtomicDouble = new AtomicDouble(1.0),
//    links: mutable.Set[Int] = new mutable.HashSet[Int]())
//  def main(args: Array[String]) {
//    val fname = args(0)
//    val g = createGraph(fname)
//    run(g)
//  }
//
////  type Graph = mutable.HashMap[Int, Page]
//  type Graph = collection.concurrent.Map[Int, Page]
//  def createGraph(fname: String) = {
//    import collection.JavaConverters._
//    val pages = new java.util.concurrent.ConcurrentHashMap[Int,Page]().asScala
//    var ctr = 0
//    scala.io.Source.fromFile(fname).getLines().filter( !_.startsWith("#"))
//      .foreach { l =>
//      val Array(src, dest) = l.split("\\t").map{_.toInt}
//      pages.getOrElseUpdate(dest, Page(dest))
//      pages.getOrElseUpdate(src, Page(src)).links += dest
//      ctr +=1
//      if (ctr % 100000 == 0) {println(s"Loaded $ctr pages")}
//    }
//    pages
//  }
//
//  val Beta = 0.8
//  def run(g: Graph, epsilon: Double = 1e-5, maxIterations: Int = 500) = {
//    val nThreads =   Runtime.getRuntime.availableProcessors
//    import concurrent._
//    var delta = Double.MaxValue
//    var iterations = 0
//
//    @inline def isMine(nThreads: Int, threadNum: Int, id: Int) = {
//      id % nThreads == threadNum
//    }
//
//    @inline def printTime = {
//      val millis = (((1.0 * System.currentTimeMillis / 1000) - (System.currentTimeMillis/1000))*1000).toInt
//      f"${new java.util.Date().toString} $millis"
//    }
//    import concurrent.ExecutionContext.Implicits.global
//    val keys = g.keySet.grouped(g.size / nThreads).toList
//    do {
//      iterations += 1
//      println(s"Starting pass #$iterations at ${printTime}")
////      println(s"Initializing  at ${printTime}")
//        val startt = System.currentTimeMillis
//      val inits = (0 until nThreads).map { threadNum =>
//        Future {
//          keys(threadNum).map{ k =>
//            val p = g(k)
//            p.rank = p.newRank.get
//            p.newRank.set(0.2)
//
//          }
//        }
//      }
//      inits.foreach{ f => f.onFailure{case e => println(s"failed inits ${e.toString}")}}
//      inits.foreach{Await.result(_, Duration.Inf)}
//
//      println(s"Updating at ${printTime}")
//      val updates = (0 until nThreads).map { threadNum =>
//        Future {
//          keys(threadNum).map{ k =>
//            val p = g(k)
//            p.links.foreach { l =>
//              g(l).newRank.getAndAdd(Beta * p.rank / p.links.size)
//            }
//          }
//        }
//      }
//      updates.foreach{ f => f.onFailure{case e => println(s"failed updates ${e.toString}")}}
//      updates.foreach{Await.result(_, Duration.Inf)}
//
//      println(s"Aggregating at ${printTime}")
//      val deltas = (0 until nThreads).map { threadNum =>
//        Future {
//          keys(threadNum).map{ k =>  g(k)}
//            .foldLeft(0.0) { case (cumdelta, p) =>
//              cumdelta + math.abs(p.rank - p.newRank.get)
//          }
//        }
//      }
//      val dvals = new Array[Double](deltas.size)
//      deltas.zipWithIndex.foreach{ case (f,ix)  =>
//        f.onComplete{
//          case Success(d) => dvals(ix) = d
//          case Failure(e) => println(s"failed deltas calcs ${e.toString}")
//        }
//      }
//      deltas.foreach{Await.result(_, Duration.Inf)}
//      val delta =  dvals.sum
//
////      val delta = deltas.map{f => f.onSuccess{  case d  => d}}.foldLeft(0.0){ case () =>
//      if (System.currentTimeMillis - startt > 1500) {
//        println(s"******* SLOW ALERT **** ${System.currentTimeMillis - startt}")
//      }
//      println(s"Finished pass #$iterations with delta=$delta at ${printTime}")
//    } while (delta > epsilon && iterations <= maxIterations)
//
//  }

}
