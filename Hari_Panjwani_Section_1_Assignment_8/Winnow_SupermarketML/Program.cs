using System;

namespace WinnowML
{
    class Program
    {
        /* 1000 is the number of rows we are going to fetch from the dataset,
            if you want to fetch less number of rows, we can change that value;
            the code will work fine, we can assume it as an argument.
        */
        static int n = 1000;

        //this are the number of features
        static int numFeatures = 88;

        // we are creating an array of array to store the dataset values.
        static int[][] data = new int[n][];       

        static void Main(string[] args)
        {   
            // setting the fileName and the location
            string filePath = "C:\\Users\\saksh\\Documents\\4th Sem\\Assignments\\Assignment 8\\supermarket.csv";

            // reading the csv file
            Console.WriteLine("Reading the csv file");
            Utils.readCSV(filePath, data, n, numFeatures);
            Console.WriteLine("Reading the csv is complete!!");

            // number of rows which are read from csv or dataset length
            Console.WriteLine("Rows of data: " + data.Length);

            //Display first and last few rows of data
            Console.WriteLine("\nFirst few lines and last of all data are: \n");
            Utils.ShowMatrix(data, 4, true);

            // Making the train test data
            Console.WriteLine("\nSplitting data into 80% train" + " and 20% test matrices");
            int[][] trainData = null;
            int[][] testData = null;
            Utils.MakeTrainTest(data, 0, out trainData, out testData);

            //Display first few rows of trainData
            Console.WriteLine("\nEncoding 'No' = 0, 'Yes' = 1, 'Low' = 0, 'High' = 1");
            Console.WriteLine("\nFirst few rows and last of training data are:\n");
            Utils.ShowMatrix(trainData, 3, true);

            //training the data
            Console.WriteLine("\nBegin training using Winnow algorithm");
            int numInput = data[0].Length - 1;
            Console.WriteLine("Number of Attributes in the data: " + numInput);
            Winnow w = new Winnow(numInput, 0); // rndSeed = 0
            double[] weights = {};            
            weights = w.TrainWeights(trainData);            
            Console.WriteLine("Training complete");

            // displaying the final weights of the features
            Console.WriteLine("\nFinal model weights are:");
            Utils.ShowVector(weights, 4, 8, true);

            //computing the accuracy
            double trainAcc = w.Accuracy(trainData);
            double testAcc = w.Accuracy(testData);

            Console.WriteLine("\nPrediction accuracy on training data = " + trainAcc.ToString("F4"));
            Console.WriteLine("Prediction accuracy on test data = " + testAcc.ToString("F4"));
            Console.ReadLine();

            //prediction
            Console.WriteLine("\nPredicting Expenditure with all features value as '1' ");

            int[] highs = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                                     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                                     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                                     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                                     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                                     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                                     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                                     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                                     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                                     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                                     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

            int predicted = w.ComputeY(highs);

            if (predicted == 0)
                Console.WriteLine("Prediction is 'Expenditure is Low'");
            else
                Console.WriteLine("Prediction is 'Expenditure is High'");

            Console.WriteLine("\nPredicting Expenditure with all features value as '0' ");

            int[] lows = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

            int predicted2 = w.ComputeY(lows);

             if (predicted2 == 0)
                Console.WriteLine("Prediction is 'Expenditure is Low'");
            else
                Console.WriteLine("Prediction is 'Expenditure is High'");


            Console.WriteLine("\nEnd of Winnow Algorithm \n");
            Console.ReadLine();

        }
    }
}
