package com.blazedb.scalatest

import org.apache.log4j.Logger

import scala.collection.mutable



case class Graph(vertices: Map[String, Vertex]) extends mutable.HashMap[String, Vertex] {
  vertices.iterator.foreach{ case (k,v) => put(k,v)}

}

object Graph {
  def apply(nVertices: Int, edgesRatio: Int) = genGraph(nVertices, edgesRatio)
  val MaxWeight = 20.0

  def genGraph(N: Int, edgesRatio: Double) = {

    val vmap = Array.tabulate(N) { ix =>
      Vertex(ix)
    }.map { v =>
      (v.name, v)
    }.toMap
    val NEdgesPerV = (edgesRatio * N).toInt
    vmap.values.foreach { v =>
      var inset = mutable.HashSet[Edge]()
      val rand = new java.util.Random
      for (neighb <- 0 until NEdgesPerV) {
        var added = false
        do {
          added = inset.add(Edge(vmap(Vertex.toName(rand.nextInt(N))),
            rand.nextInt(MaxWeight.toInt).toDouble))
        } while (!added)
      }
      v.neighbors = inset
    }
    vmap
  }
  def printGraph(graph: Graph) = {
    //    def visited = mutable.HashSet[Vertex]()
    graph.keySet.iterator.toList.sorted.mkString("Graph: ",s"${toString}","")
  }

}

case class Edge(dest: Vertex, weight: Double)

case class Vertex(name: String, var neighbors: mutable.Set[Edge] = mutable.HashSet[Edge]()) {
  import Vertex._
//  def this(idx: Int) = this(toName(idx))
  def this(idx: Int) = this(s"V$idx")

//  override def equals(that: Any) = {
//    if (!that.isInstanceOf[Vertex]) false
//    else {
//      that.asInstanceOf[Vertex].name == name
//    }
//  }
  //    override def toString = "Vertex %s: neighbors=%s}".format(name, neighbors.mkString("[", ",", "]"))
  override def toString = s"Vertex $name: neighbors=${neighbors.mkString("[", ",", "]")}"

}
object Vertex {
  def toName(i: Int): String = s"V$i"

  def apply(idx: Int) = new Vertex(idx)
}

/**
 * TSP
 * Created by sboesch on 10/13/14.
 */
object TSP {

  val logger = Logger.getLogger(getClass.getName)


  def tsp(graph: Graph, startNodeName: String) = {
    val visiteda = Array.ofDim[Boolean](graph.vertices.size,graph.vertices.size)
    val costa = Array.ofDim[Double](graph.vertices.size)

  }
  def main(args: Array[String]) {
    val graph = Graph.genGraph(5, 0.5)
  }
}