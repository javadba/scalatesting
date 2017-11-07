package com.blazedb.spark


//(2) AUC/accuracy

import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.mllib.tree.RandomForest
import org.apache.spark.mllib.tree.configuration.Strategy
import org.apache.spark.mllib.tree.model.RandomForestModel
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}


object AUC {

  def auc(spark: SparkSession) = {
    val sqlContext = spark.sqlContext
    // Paths
    val dataDir = "/mnt/mllib/regression/flightTimes/spark"
    val trainDataPath = dataDir + "/spark-train-0.1m.FIXED.parquet"
    val testDataPath = dataDir + "/spark-test.FIXED.parquet"

    // Load DataFrame, and convert to RDD of LabeledPoints
    def toLP(df: DataFrame): RDD[LabeledPoint] = {
      import spark.implicits._
      df.select("label", "features").map { case Row(label: Double, features: Vector) => LabeledPoint(label, features) }.repartition(8)
        .rdd
    }

    val train = toLP(sqlContext.read.parquet(trainDataPath)).cache()
    val test = toLP(sqlContext.read.parquet(testDataPath)).cache()
    (train.count(), test.count())

    // Train model
    val numClasses = 2
    val categoricalFeaturesInfo = Map[Int, Int]()
    val numTrees = 500
    val featureSubsetStrategy = "sqrt"
    val impurity = "gini"
    val maxDepth = 20
    val maxBins = 50

    val now = System.nanoTime
    val model = RandomForest.trainClassifier(train, numClasses, categoricalFeaturesInfo,
      numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins)
    val elapsed = (System.nanoTime - now) / 1e9
    elapsed

  }


  // Compute soft predictions. For spark.mllib trees, this works for binary classification.
  // Spark 1.5 will include it for multiclass under the spark.ml API.
  import org.apache.spark.mllib.tree.configuration.FeatureType.Continuous
  import org.apache.spark.mllib.tree.model.{DecisionTreeModel, Node}

  def softPredict(node: Node, features: Vector): Double = {
    if (node.isLeaf) {
      if (node.predict.predict == 1.0) node.predict.prob else 1.0 - node.predict.prob
    } else {
      if (node.split.get.featureType == Continuous) {
        if (features(node.split.get.feature) <= node.split.get.threshold) {
          softPredict(node.leftNode.get, features)
        } else {
          softPredict(node.rightNode.get, features)
        }
      } else {
        if (node.split.get.categories.contains(features(node.split.get.feature))) {
          softPredict(node.leftNode.get, features)
        } else {
          softPredict(node.rightNode.get, features)
        }
      }
    }
  }

  def softPredict(dt: DecisionTreeModel, features: Vector): Double = {
    softPredict(dt.topNode, features)
  }

//
//  // Compute AUC
//  val scoreAndLabels = test.map { point =>
//    //val score = model.trees.map(_.predict(point.features)).filter(_>0).size.toDouble / model.numTrees
//    val score = model.trees.map(tree => softPredict(tree, point.features)).sum / model.numTrees
//    (score, point.label)
//  }
//  val metrics = new BinaryClassificationMetrics(scoreAndLabels)
//  metrics.areaUnderROC()

}

