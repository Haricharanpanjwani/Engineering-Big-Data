//package com.bigdata.spark;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.spark.SparkConf;
//import org.apache.spark.SparkContext;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.api.java.function.Function;
//import org.apache.spark.mllib.evaluation.MulticlassMetrics;
//import org.apache.spark.mllib.linalg.DenseMatrix;
//import org.apache.spark.mllib.linalg.Matrix;
//import org.apache.spark.mllib.regression.LabeledPoint;
//import org.apache.spark.mllib.tree.RandomForest;
//import org.apache.spark.mllib.tree.model.RandomForestModel;
//import org.apache.spark.mllib.util.MLUtils;
//
//import scala.Tuple2;
//
//public class SparkML {
//
//	public static String fileheading = "C:\\Users\\saksh\\Documents\\SparkData\\";
//
//	public static void callRandomForestService() {
//
//		SparkConf sparkConf = new SparkConf().setAppName("SparkMllibRandomForest").setMaster("local");
//		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
//
//		// Load and parse the data file.
//		String datapath = fileheading  + "sparkExpedia.txt";
//		JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(jsc.sc(), datapath).toJavaRDD();
//
//		// Split the data into training and test sets (30% held out for testing)
//		JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[] { 0.7, 0.3 });
//		JavaRDD<LabeledPoint> trainingData = splits[0];
//		JavaRDD<LabeledPoint> testData = splits[1];
//
//		// Set parameters.
//		// Empty categoricalFeaturesInfo indicates all features are continuous.
//		Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();
//		Integer numTrees = 3; // Use more in practice.
//		String featureSubsetStrategy = "auto"; // Let the algorithm choose.
//		String impurity = "variance";
//		Integer maxDepth = 4;
//		Integer maxBins = 32;
//		Integer seed = 5043;
//
//		// Train a RandomForest model.
//		final RandomForestModel model = RandomForest.trainRegressor(trainingData, categoricalFeaturesInfo, numTrees,
//				featureSubsetStrategy, impurity, maxDepth, maxBins, seed);
//
//		// Evaluate model on test instances and compute test error
//		JavaRDD<Tuple2<Object, Object>> predictionAndLabel = testData.map(
//				new Function<LabeledPoint, Tuple2<Object, Object>>() {
//					public Tuple2<Object, Object> call(LabeledPoint p) {
//						Double prediction = model.predict(p.features());
//						return new Tuple2<Object, Object>(prediction, p.label());
//					}
//				});
//
//		System.out.println("Learned random forest model:\n" + model.toDebugString());
//
//		//		Double testMSE = predictionAndLabel.map(new Function<Tuple2<Double, Double>, Double>() {
//		//			public Double call(Tuple2<Double, Double> pl) {
//		//				Double diff = pl._1() - pl._2();
//		//				return diff * diff;
//		//			}
//		//		}).reduce(new Function2<Double, Double, Double>() {
//		//			public Double call(Double a, Double b) {
//		//				return a + b;
//		//			}
//		//		})/testData.count();
//
//
//		//		Double testErr =
//		//				1.0 * predictionAndLabel.filter(new Function<Tuple2<Double, Double>, Boolean>() {
//		//					public Boolean call(Tuple2<Double, Double> pl) {
//		//						return !pl._1().equals(pl._2());
//		//					}
//		//				}).count() / testData.count();
//
//		MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabel.rdd());
//
//		DenseMatrix confusion = (DenseMatrix) metrics.confusionMatrix();
//		System.out.println("Confusion matrix: \n" + confusion);
//
//		//System.out.println("Test Mean Squared Error: " + testMSE);
//
//
//		// Save and load model
//		model.save(jsc.sc(),  fileheading + "test");
//		RandomForestModel sameModel = RandomForestModel.load(jsc.sc(), fileheading + "test");
//		// $example off$
//
//		jsc.stop();
//	}
//
//	public static void main(String[] args) {
//
//		SparkConf conf = new SparkConf().setAppName("Multi class Classification Metrics Example").setMaster("local");
//		SparkContext sc = new SparkContext(conf);
//
//		// $example on$
//		String path = fileheading + "smallExpedia.txt";
//		JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(sc, path).toJavaRDD();
//
//		// Split initial RDD into two... [70% training data, 30% testing data].
//		JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.7, 0.3}, 11L);
//		JavaRDD<LabeledPoint> training = splits[0].cache();
//		JavaRDD<LabeledPoint> test = splits[1];
//
//		// Run training algorithm to build the model.
//		//		final LogisticRegressionModel model = new LogisticRegressionWithLBFGS()
//		//				.setNumClasses(100)
//		//				.run(training.rdd());
//
//		// Set parameters
//		// Empty categoricalFeaturesInfo indicates all features are continuous.
//		Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();
//		Integer numTrees = 10; // Use more in practice.
//		String featureSubsetStrategy = "sqrt"; // Let the algorithm choose.
//		String impurity = "gini";
//		Integer maxDepth = 10;
//		Integer maxBins = 32;
//		Integer seed = 5043;
//
//		// Train a RandomForest model.
//		final RandomForestModel model = RandomForest.trainClassifier(training, 100, categoricalFeaturesInfo, numTrees,
//				featureSubsetStrategy, impurity, maxDepth, maxBins, seed);
//
//		// Compute raw scores on the test set.
//		JavaRDD<Tuple2<Object, Object>> predictionAndLabels = test.map(
//				new Function<LabeledPoint, Tuple2<Object, Object>>() {
//					public Tuple2<Object, Object> call(LabeledPoint p) {
//						Double prediction = model.predict(p.features());
//						return new Tuple2<Object, Object>(prediction, p.label());
//					}
//				});
//
//		// Get evaluation metrics.
//		MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabels.rdd());
//
//		// Confusion matrix
//		Matrix confusion = metrics.confusionMatrix();
//		System.out.println("Confusion matrix: \n" + confusion);
//
//		// Overall statistics
//		System.out.println("Accuracy = " + metrics.accuracy());
//
//		// Stats by labels
//		for (int i = 0; i < metrics.labels().length; i++) {
//			System.out.format("Class %f precision = %f\n", metrics.labels()[i],metrics.precision(
//					metrics.labels()[i]));
//			System.out.format("Class %f recall = %f\n", metrics.labels()[i], metrics.recall(
//					metrics.labels()[i]));
//			System.out.format("Class %f F1 score = %f\n", metrics.labels()[i], metrics.fMeasure(
//					metrics.labels()[i]));
//		}
//
//		//Weighted stats
//		System.out.format("Weighted precision = %f\n", metrics.weightedPrecision());
//		System.out.format("Weighted recall = %f\n", metrics.weightedRecall());
//		System.out.format("Weighted F1 score = %f\n", metrics.weightedFMeasure());
//		System.out.format("Weighted false positive rate = %f\n", metrics.weightedFalsePositiveRate());
//
//		// Save and load model
//		model.save(sc, fileheading + "RandomForestModel");
//		RandomForestModel sameModel = RandomForestModel.load(sc,  fileheading + "RandomForestModel");
//		
//	}
//}
