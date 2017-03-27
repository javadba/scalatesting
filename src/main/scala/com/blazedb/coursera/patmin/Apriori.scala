package com.blazedb.coursera.patmin

import scala.collection.mutable

object Apriori {

  case class Cand(words: Array[String], supp: Int)

  def main(args: Array[String]): Unit = {
    import scala.io._
    if (args.length < 2) {
      println("Usage: Apriori <infile> <relative support> <max len>]")
      System.exit(1)
    }
    val infile = args(0)
    val relsupp = args(1).toDouble
    // val confidence = args(2).toInt
    val maxk = if (args.length >= 3) args(2).toInt else 0
    val rawd = Source.fromFile(infile).getLines.toSeq
    val txns = rawd.map { l =>
      val toks = l.split(";") // .map(_.toLowerCase)
      Set(toks: _*).toList.sorted
    }

    val cands = txns.flatMap { a =>
      a.map { w => (w, 1) }
    }.toVector.groupBy { case (a, b) => a }
      .mapValues { marr => marr.length }

    val supp = (txns.length * relsupp).toInt
    println(s"d.length=${txns.length} Support is $supp")
    val terms = cands
      .filter { case (k, v) => v >= supp }
      .map { case (k, v) => Cand(Array(k), v) }
      .toSeq

    val outSorted = terms.sortBy(-1 * _.supp)
    val fpatStr = outSorted.map(c => s"${c.supp}:${c.words.head}").mkString("", "\n", "\n")
    writeToFile("/shared/apriori.one-term.txt", fpatStr)

    val termsSet = terms.map(_.words(0)).toSet
    val indTxns = txns.map {
      _.toSet.intersect(termsSet)
    }.filter(_.nonEmpty)
      .zipWithIndex
    val indTerms = indTxns
      .flatMap { case (twlist, txid) =>
        twlist.map { word => (Set(word), txid)
        }
          .groupBy(_._1)
          .mapValues(x => x.map(_._2))
      }.map { x => (x._1, x._2.size) }
    println(s"indTxns.length=${indTxns.length}  indterms.length=${indTerms.length}")

    val kmax = 99
    var k = 2
    var fpats = mutable.ArrayBuffer(indTerms)
    var nnew = fpats.length
    var newTxns = indTxns
    while (k <= kmax && nnew > 0) {
      val patsKMinus1 = fpats.tail
      var x = 0
      val patsOnly = fpats.last.map(_._1).distinct
      val newCands1 = newTxns.map(_._1).flatMap { txn =>
        val filt = patsOnly.filter { fp =>
          fp.forall(f => txn.contains(f))
        }
        val flat = filt.flatMap { fp =>
          txn.diff(fp)
            .map { w =>
              fp + w
            }
        }.distinct
        //        if (x < 10) {
        //          println(s"FLAT: ${flat.mkString(";")}")
        //        }
        // if (flat.nonEmpty) { println(s"${flat.length}") }
        x += 1
        flat
      }.filter(_.nonEmpty)
      val newCands = newCands1.map((_, 1))
        .groupBy {
          _._1
        }
        .mapValues {
          marr => marr.length
        }
        .filter {
          _._2 >= supp
        }.toSeq
      //      newTxns = newTxns.filter { txn =>
      //        newCands.map(_._1).contains(txn._1)
      //      }
      nnew = newCands.length
      println(s"New for k=$k: ${newCands.length}")
      fpats += newCands // .map{_._1}
      k += 1
    }
    val outs = fpats.zipWithIndex.map { case (arr, x) => (x + 1, arr.length) }.mkString(",")
    println(outs)
    println(fpats.tail)

    val aout = fpats.tail.map { fp =>
      fp.sortBy{ case (set,cnt)  => -1*cnt}.map { kset => s"${kset._2}:${kset._1.mkString(";")}" }.mkString("\n")
    }.mkString("\n")
    println(aout)

    writeToFile("/shared/apriori.all-itemsets.txt", s"${fpatStr}\n${aout}")

  }

  def writeToFile(f: String, str: String) = tools.nsc.io.File(f).writeAll(str)
}

import collection.mutable.ArrayBuffer

object FpTree {

  case class FpNode(parent: Option[FpNode] = None,
    value: String = "",
    var cnt: Int = 1,
    children: ArrayBuffer[FpNode] = ArrayBuffer[FpNode]()) {

    def getTerms(): Seq[String] = if (parent.nonEmpty && parent.get.value != "") {
      parent.get.getTerms() ++ Seq(value)
    }
    else {
      Seq(value)
    }

    override def toString: String = s"$cnt:${getTerms()}"
  }

  def apply(termsList: Seq[Seq[String]]) = {
    val tree = new FpTree()
    for (terms <- termsList) {
      tree.add(terms)
    }
    tree
  }
}

class FpTree {

  //    val fptree = FpTree(ftxns)
  //    def supportCheck(supp: Int)(node: FpNode) = node.cnt >= supp
  //    val supportCheckK = supportCheck(k) _
  //
  //    val nodes = fptree.find(supportCheckK)
  //    val nodesStr = nodes.mkString(";")
  //    println(s"At level $k the nodes exceeding support=$supp include: $nodesStr")
  import FpTree._

  val heads = mutable.HashMap[String, ArrayBuffer[FpNode]]()
  val root = FpNode()

  def add(values: Seq[String]): FpNode = {
    var curNode = root
    for (value <- values) {
      curNode = add(curNode, value)
    }
    curNode
  }

  def find(pred: (FpNode) => Boolean, node: FpNode = root): Seq[FpNode] = {
    (if (pred(node)) Seq(node) else Seq.empty[FpNode]) ++ node.children.map { n =>
      find(pred, n)
    }.flatten
  }

  def add(node: FpNode, value: String): FpNode = {
    val existingNode = for (n <- node.children) yield {
      if (n.value == value) {
        n.cnt += 1
        Some(n)
      } else None.asInstanceOf[Option[FpNode]]
    }
    // .flatten
    val nn = existingNode.flatten
    val nout = if (nn.nonEmpty) {
      nn.head
    } else {
      val earlierSortedSiblings = node.children.filter(c => c.value < value).sortBy(_.value)

      def isMyNode(node: FpNode): Boolean = node.value == value

      val skipLevelBlah: ArrayBuffer[Option[FpNode]] = for (sibling <- earlierSortedSiblings) yield {
        val myNodeList = find {
          isMyNode
        }
        if (myNodeList.nonEmpty) {
          Some(add(myNodeList.head, value))
        } else {
          None
        }
      }
      val skipLevel = skipLevelBlah.flatten
      if (skipLevel.length > 1) {
        println(s"How did we get multiple matching nodes for $value : ${skipLevel.mkString(",")}")
      }
      if (skipLevel.length == 1) {
        add(skipLevel.head, value)
      } else {
        val newNode = FpNode(Some(node), value)
        node.children += newNode
        val list = heads.getOrElseUpdate(value, ArrayBuffer[FpNode]())
        list += newNode
        newNode
      }
    }
    nout
  }
}