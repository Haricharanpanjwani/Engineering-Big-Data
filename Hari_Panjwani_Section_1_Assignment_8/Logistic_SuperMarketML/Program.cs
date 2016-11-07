using System;

namespace LogisticLab
{
    public class Program
    {
        static string filePath = "C:\\Users\\saksh\\Documents\\4th Sem\\Assignments\\Assignment 8\\supermarket.csv";            
        static int numFeatures = 88; //12;
        static int numRows =  1000; //1000;
        static int seed = 42;  // interesting seeds: 28, 32, (42), 56, 58, 63, 91  
        static int maxEpochs = 1000;

        public static void Main(string[] args)
        {            
            
            // generate artificial observations
            Console.WriteLine("\nGenerating " + numRows + " artificial data items with " + numFeatures + " features");
            //double[][] data = Utils.MakeAllData(numFeatures, numRows, seed);
            double[][] data = new double[numRows][];
            Utils.readCSV(filePath, data, numRows, numFeatures);

            Console.WriteLine("\nFirst few lines and last of all data are: \n");
            Utils.ShowMatrix(data, 4, 4, true);

            // Making the train test data
            Console.WriteLine("\nSplitting data into 80% train" + " and 20% test matrices");
            double[][] trainData = null;
            double[][] testData = null;
            Utils.MakeTrainTest(data, 0, out trainData, out testData);
            
            Console.WriteLine("\nEncoding 'no' = 0, 'yes' = 1, 'low' = 0, 'high' = 1");
            Console.WriteLine("Moving political party to last column");
            Console.WriteLine("\nFirst few rows and last of training data are:\n");
            Utils.ShowMatrix(trainData, 3, 4, true);

            //training the data
            Console.WriteLine("\nBegin training using Logistic Classifier algorithm");
            int numInput = data[0].Length - 1;

            // instantiate logistic binary classifier
            Console.WriteLine("Creating LR binary classifier..");
            LogisticClassifier lc = new LogisticClassifier(numFeatures);

            // train using no regularization
            Console.WriteLine("\nStarting training using no regularization");
            double[] weights = lc.TrainWeights(trainData, maxEpochs, seed, 0, 0.0);            

            Console.WriteLine("\nBest weights found:");
            Utils.ShowVector(weights, 3, weights.Length, true);
            Console.WriteLine();

            double trainAccuracy = lc.Accuracy(trainData, weights);
            Console.WriteLine("Prediction accuracy on training data = " + trainAccuracy.ToString("F4"));

            double testAccuracy = lc.Accuracy(testData, weights);
            Console.WriteLine("Prediction accuracy on test data = " + testAccuracy.ToString("F4")); 

            //find L1
            Console.WriteLine("\nSeeking good L1 weight");
            double alpha1 = lc.FindGoodL1Weight(trainData, seed);
            Console.WriteLine("Good L1 weight = " + alpha1.ToString("F3"));
            lc.trainL1Regularization(trainData, testData, maxEpochs, seed, alpha1);
            
            //find L2
            Console.WriteLine("\nSeeking good L2 weight");
            double alpha2 = lc.FindGoodL2Weight(trainData, seed);
            Console.WriteLine("Good L2 weight = " + alpha2.ToString("F3"));
            lc.trainL1Regularization(trainData, testData, maxEpochs, seed, alpha2);

            Console.WriteLine("\nEnd Regularization\n");
            Console.ReadLine();           
        }
    }
}
