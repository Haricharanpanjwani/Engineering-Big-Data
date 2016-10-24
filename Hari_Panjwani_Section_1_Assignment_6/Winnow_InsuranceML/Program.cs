using System;
using System.IO;

namespace InsuranceML
{
    class Program
    {
        /* 188318 is the number of rows we are going to fetch from the dataset,
            if you want to fetch less number of rows, we can change that value;
            the code will work fine, we can assume it as an argument.
        */
        static int n = 188318;

        // we are creating an array of array to store the dataset values.
        static int[][] data = new int[n][];

        static int rowCount = 0;

        // this function is to read the csv, and form a matrix,
        // which is later used for training the model and later predicting
        // the severity of the claim based on the observation or features given
        static void readCSV(string filePath)
        {
            using (TextReader tr = new StreamReader(filePath))
            {
                //Processing the csv file now
                String str;
                while ((str = tr.ReadLine()) != null)
                {
                    string[] fields = str.Split(',');
                    int[] num = new int[fields.Length - 1];
                    /*
                        Insurance dataset has categorical value as A and B, so for implementing 
                        the winnow, we have  to convert those A and B into 0 and 1 respectively, 
                        apart from this the predictor colum is also a continuous variable, for 
                        converting that  into binary, we are taking mean which is computed in R, and if 
                        the value is greater than mean than severity is 1, else it is not sever i.e., 0

                        We are running th for loop 74 times because there are 72 categorical variables, and
                        starting the loop from 1, because 1st colum is ID, which is not relevant to us.
                    */
                    for (int i = 1; i < 74; i++)
                    {
                        if (rowCount == 0)
                            break;

                        //Console.WriteLine(i + " " + fields[i]);
                        if (fields[i] == "A")
                            num[i - 1] = 0;
                        else if (fields[i] == "B")
                            num[i - 1] = 1;
                        else if (Double.Parse(fields[i]) > 13780)
                            num[i - 1] = 1;
                        else
                            num[i - 1] = 0;
                    }

                    if (rowCount != 0)
                        data[rowCount - 1] = num;                                        

                    if(rowCount == n)
                    break;

                    rowCount++;
                }
            }
        }

        // this function is to display first and last few rows of the dataset,
        // it take the dataset which we formed, the number of rows you want to
        // display and boolean indices, do you want to show the index number of row or not
        static void ShowMatrix(int[][] matrix, int numRows, bool indices)
        {
            for (int i = 0; i < numRows; ++i)
            {
                if (indices == true)
                    Console.Write("[" + i.ToString().PadLeft(2) + "]   ");
                for (int j = 0; j < matrix[i].Length; ++j)
                {
                    Console.Write(matrix[i][j] + " ");
                }
                Console.WriteLine("");
            }
            int lastIndex = matrix.Length - 1;
            if (indices == true)
                Console.Write("[" + lastIndex.ToString().PadLeft(2) + "]   ");
            for (int j = 0; j < matrix[lastIndex].Length; ++j)
                Console.Write(matrix[lastIndex][j] + " ");
            Console.WriteLine("\n");
        }

        /*
            This function make train and test data from the dataset, we split the data into 80% and 20%,
            80% is to train the model based on Winnow Algorithm and rest 20% is to predict the accuracy 
            of the algorithm. Before splitting the dataset, we set the seed value, seed value is a Random
            number which is used for generating the same trainData again.
        */
        static void MakeTrainTest(int[][] data, int seed, out int[][] trainData, out int[][] testData)
        {
            Random rnd = new Random(seed);
            int totRows = data.Length; // compute number of rows in each result
            int numTrainRows = (int)(totRows * 0.80);
            int numTestRows = totRows - numTrainRows;
            trainData = new int[numTrainRows][];
            testData = new int[numTestRows][];

            int[][] copy = new int[data.Length][]; // make a copy of data
            for (int i = 0; i < copy.Length; ++i)  // by reference to save space
                copy[i] = data[i];
            for (int i = 0; i < copy.Length; ++i) // scramble row order of copy
            {
                int r = rnd.Next(i, copy.Length);
                int[] tmp = copy[r];
                copy[r] = copy[i];
                copy[i] = tmp;
            }
            for (int i = 0; i < numTrainRows; ++i) // create training
                trainData[i] = copy[i];

            for (int i = 0; i < numTestRows; ++i) // create test
                testData[i] = copy[i + numTrainRows];
        } // MakeTrainTest

        /*
            This function is used to display the final weights of the features, which is
            assigned to them by them by Winnow, based on the update function of winnow,
            it take number of features display in the row and whether you want everything
            to be in one line or more.
        */
        static void ShowVector(double[] vector, int decimals, int valsPerRow, bool newLine)
        {
            for (int i = 0; i < vector.Length; ++i)
            {
                if (i % valsPerRow == 0) Console.WriteLine("");
                Console.Write(vector[i].ToString("F" + decimals).PadLeft(decimals + 4) + " ");
            }
            if (newLine == true) Console.WriteLine("");
        }        

        static void Main(string[] args)
        {   
            // setting the fileName and the location
            string filePath = "C:\\Users\\saksh\\Documents\\4th Sem\\Assignments\\Assignment 6\\InsuranceData.csv";

            //  reading the csv file
            Console.WriteLine("Reading the csv file");
            readCSV(filePath);
            Console.WriteLine("Reading the csv is complete!!");
            Console.ReadLine();

            // number of rows which are read from csv or dataset length
            Console.WriteLine("Rows of data: " + data.Length);
            Console.ReadLine();

            //Display first and last few rows of data
            Console.WriteLine("\nFirst few lines and last of all data are: \n");
            ShowMatrix(data, 4, true);
            Console.ReadLine();

            // Making the train test data
            Console.WriteLine("\nSplitting data into 80% train" + " and 20% test matrices");
            int[][] trainData = null;
            int[][] testData = null;
            MakeTrainTest(data, 0, out trainData, out testData);

            //Display first few rows of trainData
            Console.WriteLine("\nEncoding 'A' = 0, 'B' = 1, 'Not Sever' = 0, 'Sever' = 1");
            Console.WriteLine("\nFirst few rows and last of training data are:\n");
            ShowMatrix(trainData, 3, true);

            //training the data
            Console.WriteLine("\nBegin training using Winnow algorithm");
            int numInput = data[0].Length - 1;
            Console.WriteLine("Number of Attributes in the data: " + numInput);
            Winnow w = new Winnow(numInput, 0); // rndSeed = 0
            double[] weights = w.TrainWeights(trainData);
            Console.WriteLine("Training complete");
            Console.ReadLine();

            // displaying the final weights of the features
            Console.WriteLine("\nFinal model weights are:");
            ShowVector(weights, 4, 8, true);
            Console.ReadLine();

            //computing the accuracy
            double trainAcc = w.Accuracy(trainData);
            double testAcc = w.Accuracy(testData);

            Console.WriteLine("\nPrediction accuracy on training data = " + trainAcc.ToString("F4"));
            Console.WriteLine("Prediction accuracy on test data = " + testAcc.ToString("F4"));
            Console.ReadLine();

            //prediction
            Console.WriteLine("\nPredicting insurance claim severity with all features value as '1' ");

            int[] yays = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                                     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                                     1, 1, 1, 1, 1, 1, 1, 1 };

            int predicted = w.ComputeY(yays);

            if (predicted == 0)
                Console.WriteLine("Prediction is 'Insurance claim is not Sever'");
            else
                Console.WriteLine("Prediction is 'Insurance claim is Sever'");

            Console.WriteLine("\nPredicting insurance claim severity with all features value as '0' ");

            int[] nays = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                     0, 0, 0, 0, 0, 0, 0, 0 };
            int predicted2 = w.ComputeY(nays);

             if (predicted2 == 0)
                Console.WriteLine("Prediction is 'Insurance claim is not Sever'");
            else
                Console.WriteLine("Prediction is 'Insurance claim is Sever'");


            Console.WriteLine("\nEnd of Winnow Algorithm on Allstate's Insurance Claim Dataset\n");

            Console.ReadLine();

        }
    }
}
