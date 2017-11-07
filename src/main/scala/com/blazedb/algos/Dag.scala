package com.blazedb.algos

/**
  * MZ ianterview of SBoesch 9/29/17  by Bikrant N
  *
  */
object Dag {

  type ID = Int

  case class Node(id: ID, nbrs: Set[ID]) {
    override def equals(that: Any) = that.isInstanceOf[Node] && id == that.asInstanceOf[Node].id
  }

  def isDag(nSet: Seq[Node]) = {
    import collection.mutable.Map

    val nodes = nSet.foldLeft(Map.empty[Int,Node]) { case (map,nd) => map(nd.id)=nd; map }

    def isDag0(nd: Node, visited: Set[ID] = Set.empty[ID]): (Boolean, Set[ID]) = {
      if (visited(nd.id)) {
        println(s"NOT a DAG: Already visited ${nd.id}")
        (false, visited)
      } else {
        val childResults = for (ndChildId <- nd.nbrs) yield {
          println(s"${nd.id}->$ndChildId")
          val (childIsDag, childVisited) = isDag0(nodes(ndChildId), visited + nd.id)
          (childIsDag, childVisited)
        }
        childResults.foldLeft((true,visited)) { case ((cumIsDag, allVisited), (childIsDag, childVisited)) =>
          (cumIsDag && childIsDag, allVisited ++ childVisited)
        }
      }
    }

    val (finalAnswer, allVisitedNodes) = nSet.foldLeft((true, Set.empty[ID])) { case ((cumIsDag, allVisited), node) =>
      if (!allVisited.contains(node.id)) {
        val (newIsDag, newVisited) = isDag0(node, Set.empty[ID])
        (newIsDag && cumIsDag, allVisited ++ newVisited)
      } else { // Already visited this node
        (cumIsDag, allVisited)
      }
    }
    finalAnswer
  }

}

object DagTest {

  import Dag._

  /*
    Output

    2->3
    1->2
    2->3
    1->3
    happyCaseDag: List(Node(3,Set()), Node(2,Set(3)), Node(1,Set(2, 3))) true
    2->3
    3->1
    1->2
    NOT a DAG: Already visited 2
    cyclicCase: List(Node(2,Set(3)), Node(1,Set(2)), Node(3,Set(1)))  false
    3->4
    2->3
    3->4
    2->4
    1->2
    2->3
    3->4
    2->4
    1->3
    3->4
    biggerHappyCaseDag: List(Node(3,Set(4)), Node(2,Set(3, 4)), Node(1,Set(2, 3)), Node(4,Set())) true
    3->4
    4->2
    2->3
    NOT a DAG: Already visited 3
    1->2
    2->3
    3->4
    4->2
    NOT a DAG: Already visited 2
    1->3
    3->4
    4->2
    2->3
    NOT a DAG: Already visited 3
    biggerCyclicCase: List(Node(3,Set(4)), Node(2,Set(3)), Node(1,Set(2, 3)), Node(4,Set(2))) false

 */
  def main (args: Array[String] ): Unit = {
     /*
            1
           / \
          v   v
          2-->3

    */
    val happyCaseDag = Seq(Node(3,Set.empty[ID]),Node(2,Set(3)), Node(1,Set(2,3)))
    println(s"happyCaseDag: $happyCaseDag ${isDag(happyCaseDag)}" )

     /*
            1
           / ^
          v   \
          2-->3

    */
    val cyclicCase = Seq(Node(2,Set(3)),Node(1,Set(2)), Node(3,Set(1)))
    println(s"cyclicCase: $cyclicCase  ${isDag(cyclicCase)}" )

     /*
            1
           / \
          v   v
          2-->3
          \   /
           v v
            4

    */

    val biggerHappyCaseDag = Seq(Node(3,Set(4)),Node(2,Set(3,4)), Node(1,Set(2,3)), Node(4,Set.empty[ID]))
    println(s"biggerHappyCaseDag: $biggerHappyCaseDag ${isDag(biggerHappyCaseDag)}" )


     /*
            1
           / \
          v   v
          2-->3
          ^   /
           \ v
            4

    */

    val biggerCyclicCase = Seq(Node(3,Set(4)),Node(2,Set(3)), Node(1,Set(2,3)), Node(4,Set(2)))
    println(s"biggerCyclicCase: $biggerCyclicCase ${isDag(biggerCyclicCase)}" )


  }
}
