package com.blazedb.spark

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.spark.{Partitioner, SparkContext}


object SparkTest {

    def main(args: Array[String]) {
      val sc = new SparkContext("local[2]", "mptest")
      val brad = sc.textFile("file:///shared/bradtest.csv")
      val xrdd = brad.map{ l => l.split(",")}
      val alldat = xrdd.collect()
      val headers = alldat(0)
      val data = alldat.tail
      println("headers:%s  data(0):%s",headers.mkString("[",",","]"), data(0).mkString("[",",","]"))
    }

}