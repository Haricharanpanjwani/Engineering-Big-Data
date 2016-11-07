using System;
using System.IO;

namespace WinnowML {
    
    class Utils {
        
        // this function is to read the csv, and form a matrix,
        // which is later used for training the model and later predicting
        // the severity of the claim based on the observation or features given
        public static void readCSV(string filePath, int[][] data, int n, int numFeatures)
        {
            using (TextReader tr = new StreamReader(filePath))
            {
                int rowCount = 0;;

                //Processing the csv file now
                String str;
                while ((str = tr.ReadLine()) != null)
                {
                    string[] fields = str.Split(',');
                    int[] num = new int[fields.Length];

                    for (int i = 0; i < numFeatures; i++)
                    {
                        if (rowCount == 0)
                            break;

                        //Console.WriteLine(rowCount + " " + i + " " + fields[i]);
                        num[i] = Int32.Parse(fields[i]);
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
        public static void ShowMatrix(int[][] matrix, int numRows, bool indices)
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
        public static void MakeTrainTest(int[][] data, int seed, out int[][] trainData, out int[][] testData)
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
        public static void ShowVector(double[] vector, int decimals, int valsPerRow, bool newLine)
        {
            for (int i = 0; i < vector.Length; ++i)
            {
                if (i % valsPerRow == 0) Console.WriteLine("");
                Console.Write(vector[i].ToString("F" + decimals).PadLeft(decimals + 4) + " ");
            }
            if (newLine == true) Console.WriteLine("");
        } 
    }
}