package com.blazedb.ml

import java.io.File

import org.apache.avro.mapred.SequenceFileReader
import org.apache.hadoop.io.Text
//import org.apache.mahout.math.{Vector => MVector}
import org.apache.spark.SparkContext
import org.scalatest.FunSuite

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

/**
 * MahoutMathTest
 *
 */
class MahoutMathTest extends FunSuite {
  test("vector") {
    val N = 20
    val dir = "/some/hdfs/dir"
//    val sfreader= new SequenceFileReader[Text, MVector](new File(dir))
    val sc = new SparkContext("local[2]", "MahoutTest")
//    val sfData = sc.sequenceFile[NullWritable, MVector](dir)
//    val xformedVectors = sfData.map { case (label, vect) =>
//      import scala.collection.JavaConversions._
//      (label, Vectors.dense(vect.all.iterator.map{ e => e.get}.toArray))
//    }

  }

}
