package com.blazedb.spark

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.spark.{Partition, Partitioner, SparkContext}

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

case class SimulatedHivePartitioner(hdfsPath: String) extends Partitioner {

  def computePartitions() = {
    //      val f : FileInputFormat = _
    //      f.
    null.asInstanceOf[Seq[Partition]]
  }

  override def numPartitions: Int = ???

  override def getPartition(key: Any): Int = ???
}
  ///**
  // * MapPartitionsTest
  // *
  // */
  //object MapPartitionsTest {
  //
  //  def runtest(sc: SparkContext, cnt: Int, nParts: Int): Unit = {
  //    val rand = new util.Random
  //    val rdd1 = sc.parallelize(Array.tabulate(cnt) { i =>
  //      val x = rand.nextInt(cnt)
  //      (x, s"abcde$x!")
  //    },nParts)
  //
  //    val repart1 = rdd1.groupBy(
  //    val rdd2 = sc.parallelize(Array.tabulate(cnt) { i =>
  //      val x = rand.nextInt(cnt)
  //      (x, s"abcde$x!")
  //    },nParts)
  //
  //  }
  //  def main(args: Array[String]) {
  //    val sc = new SparkContext("local[2]", "mptest")
  //      sc.textFile
  //  }
  //
  //}
//}