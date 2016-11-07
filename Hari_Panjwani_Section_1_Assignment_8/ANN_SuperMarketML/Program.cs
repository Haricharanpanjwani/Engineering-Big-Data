using System;
using System.IO;

namespace GenericAlgorithm
{
    public class Program
    {
        public static void Main(string[] args)
        {

            int numFeatures = 88; //12;
            int numRows =  1000; //1000;
       
            string filePath = "C:\\Users\\saksh\\Documents\\4th Sem\\Assignments\\Assignment 8\\supermarket.csv";

            double[][] allData = new double[numRows][];        

            //double[][] allData = Utils.LoadData("IrisData3.txt", 150, 7); // 150 rows, 7 cols
            allData = Utils.readCSV(filePath, allData, numFeatures, numRows); // 150 rows, 7 cols
            Console.WriteLine("Goal is to predict expenditure of the grocery,");
            Console.WriteLine("Low = (1,0), High = (0,1)");
            Console.WriteLine("\nThe 1000-item data set is:\n");
            Utils.ShowMatrix(allData, 4, 1, true);

            double[][] trainData = null;
            double[][] testData = null;
            double trainPct = 0.80;
            int splitSeed = 1;
            Console.WriteLine("Splitting data into 80% train, 20% test");
            Utils.SplitData(allData, trainPct, splitSeed, out trainData, out testData);
            Console.WriteLine("\nThe training data is:\n");     
            Utils.ShowMatrix(trainData, 4, 1, true);
            Console.WriteLine("The test data is:\n");     
            Utils.ShowMatrix(testData, 3, 1, true);

            // training parameters specific to EO
            // population size
            int popSize = 1000; 
            
            // maximum iterations
            int maxGeneration = 2000;

            /*
              short-circuit exit threshold if the best set of 
              weights found produces an error less than the threshold. 
              Here, early exit not occurs
            */
            double exitError = 0.0; 

            //how many genes in a newly-generated child's chromosome will be mutated
            double mutateRate = 0.20;

            //magnitude of change of mutated genes            
            double mutateChange = 0.01; //0.01
            
            /*
                "selection pressure" controls the likelihood that the two best individuals 
                in the population will be selected as parents for reproduction (Larger values of tau increase the 
                chances that the two best individuals will be chosen)
             */
            double tau = 0.40; //0.40

            Console.WriteLine("\nSetting popSize = " + popSize);
            Console.WriteLine("Setting maxGeneration = " + maxGeneration);
            Console.Write("Setting early exit MSE error = ");
            Console.WriteLine(exitError.ToString("F3"));
            Console.Write("Setting mutateRate = ");
            Console.WriteLine(mutateRate.ToString("F3"));
            Console.Write("Setting mutateChange = ");
            Console.WriteLine(mutateChange.ToString("F3"));
            Console.Write("Setting tau = ");
            Console.WriteLine(tau.ToString("F3"));


            Console.WriteLine("Creating a 85-60-2 neural network");
            Console.WriteLine("Using tanh and softmax activations");
            const int numInput = 85;
            const int numHidden = 60;
            const int numOutput = 2;
            ANN nn = new ANN(numInput, numHidden, numOutput);

            Console.WriteLine("\nBeginning training");
            double[] bestWeights = nn.Train(trainData, popSize, maxGeneration, exitError,
            mutateRate, mutateChange, tau);
            Console.WriteLine("Training complete");

            // Begin training of the model
            Console.WriteLine("\nFinal weights and bias values:");
            Utils.ShowVector(bestWeights, 4, 8, true);
            
            // setting the best weights
            nn.SetWeights(bestWeights);

            // getting the accuracy of the model
            double trainAcc = nn.Accuracy(trainData);
            Console.Write("\nAccuracy on training data = ");
            Console.WriteLine(trainAcc.ToString("F4"));

            double testAcc = nn.Accuracy(testData);
            Console.Write("\nAccuracy on test data = ");
            Console.WriteLine(testAcc.ToString("F4"));

            Console.ReadLine();
        }
    }
}
