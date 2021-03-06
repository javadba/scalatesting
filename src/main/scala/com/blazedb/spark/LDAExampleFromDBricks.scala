package com.blazedb.spark

import org.apache.spark.ml.clustering.LDAModel
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

object LDAExampleFromDBricks {

  def lda(spark: SparkSession) = {
    /*
This example uses Scala.  Please see the MLlib documentation for a Java example.

Try running this code in the Spark shell.  It may produce different topics each time (since LDA includes some randomization), but it should give topics similar to those listed above.

This example is paired with a blog post on LDA in Spark: http://databricks.com/blog
*/

    import scala.collection.mutable
    import org.apache.spark.ml.clustering.LDA
    import org.apache.spark.ml.linalg.{Vector, Vectors}
    import org.apache.spark.rdd.RDD

    val sc = spark.sparkContext
    // Load documents from text files, 1 document per file
    val corpus: RDD[String] = sc.wholeTextFiles("docs/*.md").map(_._2)

    // Split each document into a sequence of terms (words)
    val tokenized: RDD[Seq[String]] =
      corpus.map(_.toLowerCase.split("\\s")).map(_.filter(_.length > 3).filter(_.forall(java.lang.Character.isLetter)))

    // Choose the vocabulary.
    //   termCounts: Sorted list of (term, termCount) pairs
    val termCounts: Array[(String, Long)] =
    tokenized.flatMap(_.map(_ -> 1L)).reduceByKey(_ + _).collect().sortBy(-_._2)
    //   vocabArray: Chosen vocab (removing common terms)
    val numStopwords = 20
    val vocabArray: Array[String] =
      termCounts.takeRight(termCounts.size - numStopwords).map(_._1)
    //   vocab: Map term -> term index
    val vocab: Map[String, Int] = vocabArray.zipWithIndex.toMap

    // Convert documents into term count vectors
    val documents: RDD[(Long, Vector)] =
      tokenized.zipWithIndex.map { case (tokens, id) =>
        val counts = new mutable.HashMap[Int, Double]()
        tokens.foreach { term =>
          if (vocab.contains(term)) {
            val idx = vocab(term)
            counts(idx) = counts.getOrElse(idx, 0.0) + 1.0
          }
        }
        (id, Vectors.sparse(vocab.size, counts.toSeq))
      }

    // Set LDA parameters
    val numTopics = 10
    val lda = new LDA().setK(numTopics).setMaxIter(10)

    import spark.sqlContext.implicits._
    val df = documents.toDF
    val ldaModel = lda.fit(df)
    val avgLogLikelihood = ldaModel.asInstanceOf[LDAModel].logLikelihood(df) / documents.count

    // Print topics, showing top-weighted 10 terms for each topic.
    val topicIndices = ldaModel.describeTopics(maxTermsPerTopic = 10)
//    topicIndices.rdd.foreach { case (terms, termWeights) =>
//      println("TOPIC:")
//      terms.zip(termWeights).foreach { case (term, weight) =>
//        println(s"${vocabArray(term.toInt)}\t$weight")
//      }
//      println()
//    }
  }

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("ldatest")
    val sc = new SparkContext(conf)
    val spark = SparkSession.builder.getOrCreate
    lda(spark)
  }
}
