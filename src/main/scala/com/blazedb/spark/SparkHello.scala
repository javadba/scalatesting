package com.blazedb.spark

import org.apache.spark.{SparkConf, SparkContext}

object SparkHello {
  def main(args: Array[String]): Unit = {
    val master = if (args.length >=1) args(0) else s"spark://${java.net.InetAddress.getLocalHost.getHostName}:7077"
    val conf = new SparkConf().setMaster(master).setAppName("SparkHello")
    val sc = new SparkContext(conf)
    val coll = "SparkHello"
    val rdd = sc.parallelize( coll.toCharArray.sorted.map{ _.toInt})
    val res = rdd.collect().sum
    println(s"sum of chars of $coll = $res")
  }

}
