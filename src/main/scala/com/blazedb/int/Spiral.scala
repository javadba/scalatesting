package com.blazedb.int

// 5 = 01 02 03 04 05
//     16 17 18 19 06
//     15 24 25 20 07
//     14 23 22 21 08
//     13 12 11 10 09

// To execute Scala, please define an object named Solution that extends App

object Solution extends App {

  def spiral(N: Int) = {

    val formatLen = (N*N).toString.length

    val emptyVal = new String(new Array[Char](formatLen)).replace("\0", "0")
    @inline def pad(n: Int) =
      emptyVal.substring(0,formatLen-n.toString.length) + n

    // import collection.mutable
    // val a = Array.tabulate(N){ Array.fill(N)(0)}
    val a = Array.ofDim[String](N, N)
    var pivotDir = 0
    var i = 1
    var len = N-1
    var spiralNum = 0
    do {
      assert(len >= 0, "buddy you got bugs")
      var j = 0
      for (j <- 0 until len) {
        a(spiralNum)(spiralNum+j) = pad(i + j)
      }
      i+=len
      for (j <- 0 until len) {
        a(spiralNum+j)(N - 1 - spiralNum) = pad(i + j)
      }
      i+=len
      for (j <- 0 until len) {
        a(N - 1 - spiralNum)(N - 1 - j-spiralNum) = pad(i + j)
      }
      i+=len
      for (j <- 0 until len) {
        a(N - 1 - j-spiralNum)(spiralNum) = pad(i + j)
      }
      i += len
      len -= 2
      spiralNum += 1
      println(formatMat(N, a))

    } while (i < N * N - 1)
    a(N/2)(N/2) = pad(i)
    a
  }


  def formatMat(N: Int, a: Array[Array[String]]) = {
    val formatLen = (N*N).toString.length
    val emptyVal = new String(new Array[Char](formatLen)).replace("\0", "0")
    val printable = a.foldLeft("") { case (str, arr) =>
      s"$str\n${arr.map(s => if (s==null) emptyVal else s).mkString(" ")}"
    }
    printable
  }
  val N = 5
  val matrix = spiral(N)
  println(formatMat(N, matrix))
}