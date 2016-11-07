using System;

namespace LogisticLab
{
    public class LogisticClassifier
    {
        private int numFeatures; // number of independent variables aka features
        private double[] weights; // b0 = constant
        private Random rnd;

        public LogisticClassifier(int numFeatures)
        {
            this.numFeatures = numFeatures; // number features/predictors
            this.weights = new double[numFeatures + 1]; // [0] = b0 constant
            this.rnd = new Random(0); // seed could be a parmeter to ctor
        }

        // function to find the value of alpha for L1 regularization
        public double FindGoodL1Weight(double[][] trainData, int seed)
        {
            double result = 0.0;
            double bestErr = double.MaxValue;
            double currErr = double.MaxValue;
            double[] candidates = new double[] { 0.000, 0.001, 0.005, 0.010, 0.020, 0.050, 0.100, 0.150 };
            int maxEpochs = 1000;

            LogisticClassifier c = new LogisticClassifier(this.numFeatures);

            for (int trial = 0; trial < candidates.Length; ++trial)
            {
                double alpha1 = candidates[trial];
                double[] wts = c.TrainWeights(trainData, maxEpochs, seed, alpha1, 0.0);
                currErr = Error(trainData, wts, 0.0, 0.0);
                if (currErr < bestErr)
                {
                    bestErr = currErr;
                    result = candidates[trial];
                }
            }
            return result;
        }

        // function to find the value of alpha for L2 regularization
        public double FindGoodL2Weight(double[][] trainData, int seed)
        {
            double result = 0.0;
            double bestErr = double.MaxValue;
            double currErr = double.MaxValue;
            double[] candidates = new double[] { 0.000, 0.001, 0.005, 0.010, 0.020, 0.050, 0.100, 0.150 };
            int maxEpochs = 1000;

            LogisticClassifier c = new LogisticClassifier(this.numFeatures);

            for (int trial = 0; trial < candidates.Length; ++trial)
            {
                double alpha2 = candidates[trial];
                double[] wts = c.TrainWeights(trainData, maxEpochs, seed, 0.0, alpha2);
                currErr = Error(trainData, wts, 0.0, 0.0);
                if (currErr < bestErr)
                {
                    bestErr = currErr;
                    result = candidates[trial];
                }
            }
            return result;
        }

        // train the model using No regularization
        public double[] TrainWeights(double[][] trainData, int maxEpochs, int seed, double alpha1, double alpha2)
        {
            // use PSO. particle position == LR weights
            int numParticles = 10;
            double probDeath = 0.005;
            int dim = this.numFeatures + 1; // need one wt for each feature, plus the b0 constant

            Random rnd = new Random(seed);

            int epoch = 0;
            double minX = -10.0; // for each weight. assumes data has been normalized about 0
            double maxX = 10.0;
            double w = 0.729; // inertia weight
            double c1 = 1.49445; // cognitive/local weight
            double c2 = 1.49445; // social/global weight
            double r1, r2; // cognitive and social randomizations

            Particle[] swarm = new Particle[numParticles];
            // best solution found by any particle in the swarm. implicit initialization to all 0.0
            double[] bestSwarmPosition = new double[dim];
            double bestSwarmError = double.MaxValue; // smaller values better

            // initialize each Particle in the swarm with random positions and velocities
            for (int i = 0; i < swarm.Length; ++i)
            {
                double[] randomPosition = new double[dim];
                for (int j = 0; j < randomPosition.Length; ++j)
                    randomPosition[j] = (maxX - minX) * rnd.NextDouble() + minX;

                // randomPosition is a set of weights
                double error = Error(trainData, randomPosition, alpha1, alpha2);
                double[] randomVelocity = new double[dim];
                for (int j = 0; j < randomVelocity.Length; ++j)
                {
                    double lo = 0.1 * minX;
                    double hi = 0.1 * maxX;
                    randomVelocity[j] = (hi - lo) * rnd.NextDouble() + lo;
                }
                swarm[i] = new Particle(randomPosition, error, randomVelocity,
                    randomPosition, error); // last two are best-position and best-error

                // does current Particle have global best position?
                // best position for the particle is the one that's closest to the label (Y)
                if (swarm[i].error < bestSwarmError)
                {
                    bestSwarmError = swarm[i].error;
                    swarm[i].position.CopyTo(bestSwarmPosition, 0);
                }
            }

            // main PSO algorithm
            int[] sequence = new int[numParticles]; // process particles in random order
            for (int i = 0; i < sequence.Length; ++i)
                sequence[i] = i;

            while (epoch < maxEpochs)
            {
                double[] newVelocity = new double[dim]; // step 1
                double[] newPosition = new double[dim]; // step 2
                double newError; // step 3

                Shuffle(sequence); // move particles in random sequence

                for (int pi = 0; pi < swarm.Length; ++pi) // each Particle (index)
                {
                    int i = sequence[pi];
                    Particle currP = swarm[i]; // for coding convenience

                    // 1. compute new velocity
                    for (int j = 0; j < currP.velocity.Length; ++j) // each x value of the velocity
                    {
                        r1 = rnd.NextDouble();
                        r2 = rnd.NextDouble();

                        // velocity depends on old velocity, best position of particle, and 
                        // best position of any particle
                        newVelocity[j] = (w * currP.velocity[j]) +
                            (c1 * r1 * (currP.bestPosition[j] - currP.position[j])) +
                            (c2 * r2 * (bestSwarmPosition[j] - currP.position[j]));
                    }

                    newVelocity.CopyTo(currP.velocity, 0);

                    // 2. use new velocity to compute new position
                    for (int j = 0; j < currP.position.Length; ++j)
                    {
                        newPosition[j] = currP.position[j] + newVelocity[j];  // compute new position
                        if (newPosition[j] < minX) // keep in range
                            newPosition[j] = minX;
                        else if (newPosition[j] > maxX)
                            newPosition[j] = maxX;
                    }

                    newPosition.CopyTo(currP.position, 0);

                    // 3. use new position to compute new error
                    newError = Error(trainData, newPosition, alpha1, alpha2);
                    currP.error = newError;

                    if (newError < currP.bestError) // new particle best?
                    {
                        newPosition.CopyTo(currP.bestPosition, 0);
                        currP.bestError = newError;
                    }

                    if (newError < bestSwarmError) // new swarm best?
                    {
                        newPosition.CopyTo(bestSwarmPosition, 0);
                        bestSwarmError = newError;
                    }

                    // 4. optional: does curr particle die?
                    double die = rnd.NextDouble();
                    if (die < probDeath)
                    {
                        // new position, leave velocity, update error
                        for (int j = 0; j < currP.position.Length; ++j)
                            currP.position[j] = (maxX - minX) * rnd.NextDouble() + minX;
                        currP.error = Error(trainData, currP.position, alpha1, alpha2);
                        currP.position.CopyTo(currP.bestPosition, 0);
                        currP.bestError = currP.error;

                        if (currP.error < bestSwarmError) // swarm best by chance?
                        {
                            bestSwarmError = currP.error;
                            currP.position.CopyTo(bestSwarmPosition, 0);
                        }
                    }

                } // each Particle
                ++epoch;
            } // while

            double[] retResult = new double[dim];
            Array.Copy(bestSwarmPosition, retResult, retResult.Length);
            return retResult;
        } // TrainWeights

        // train using L1 regularization
        public void trainL1Regularization(double[][] trainData, double[][] testData, int maxEpochs, int seed, double alpha1)
        {
            
            Console.WriteLine("\nStarting training using L1 regularization, alpha1 = " + alpha1.ToString("F3"));
            double[] weights = TrainWeights(trainData, maxEpochs, seed, alpha1, 0.0);

            Console.WriteLine("\nBest weights found:");
            Utils.ShowVector(weights, 3, weights.Length, true);

            double trainAccuracy = Accuracy(trainData, weights);
            Console.WriteLine("Prediction accuracy on training data = " + trainAccuracy.ToString("F4"));

            double testAccuracy = Accuracy(testData, weights);
            Console.WriteLine("Prediction accuracy on test data = " + testAccuracy.ToString("F4"));
        }

        // train using L2 regularization
        public void trainL2Regularization(double[][] trainData, double[][] testData, int maxEpochs, int seed, double alpha2)
        {            
            Console.WriteLine("\nStarting training using L2 regularization, alpha2 = " + alpha2.ToString("F3"));
            double[] weights = TrainWeights(trainData, maxEpochs, seed, 0.0, alpha2);

            Console.WriteLine("\nBest weights found:");
            Utils.ShowVector(weights, 3, weights.Length, true);

            double trainAccuracy = Accuracy(trainData, weights);
            Console.WriteLine("Prediction accuracy on training data = " + trainAccuracy.ToString("F4"));

            double testAccuracy = Accuracy(testData, weights);
            Console.WriteLine("Prediction accuracy on test data = " + testAccuracy.ToString("F4"));            
        }

        // shuffle the data before sending it to the training
        private void Shuffle(int[] sequence)
        {
            for (int i = 0; i < sequence.Length; ++i)
            {
                int r = rnd.Next(i, sequence.Length);
                int tmp = sequence[r];
                sequence[r] = sequence[i];
                sequence[i] = tmp;
            }
        }

        // compute error while training the model
        public double Error(double[][] trainData, double[] weights, double alpha1, double alpha2)
        {
            // mean squared error using supplied weights
            // L1 regularization adds the sum of the absolute values of the weights
            // L2 regularization adds the sqrt of sum of squared values

            int yIndex = trainData[0].Length - 1; // y-value (0/1) is last column
            double sumSquaredError = 0.0;
            for (int i = 0; i < trainData.Length; ++i) // each data
            {
                double computed = ComputeY(trainData[i], weights);
                double desired = trainData[i][yIndex]; // ex: 0.0 or 1.0
                sumSquaredError += (computed - desired) * (computed - desired);
            }

            double sumAbsVals = 0.0; // L1 penalty
            for (int i = 0; i < weights.Length; ++i)
                sumAbsVals += Math.Abs(weights[i]);

            double sumSquaredVals = 0.0; // L2 penalty
            for (int i = 0; i < weights.Length; ++i)
                sumSquaredVals += (weights[i] * weights[i]);
            //double rootSum = Math.Sqrt(sumSquaredVals);

            return (sumSquaredError / trainData.Length) +
                (alpha1 * sumAbsVals) +
                (alpha2 * sumSquaredVals);
        }

        // compute the dependent variable based on the observation
        public double ComputeY(double[] dataItem, double[] weights)
        {
            double z = 0.0;

            z += weights[0]; // the b0 constant
            for (int i = 0; i < weights.Length - 1; ++i) // data might include Y
                z += (weights[i + 1] * dataItem[i]); // skip first weight

            return 1.0 / (1.0 + Math.Exp(-z));
        }

        public int ComputeDependent(double[] dataItem, double[] weights)
        {
            double sum = ComputeY(dataItem, weights);

            if (sum <= 0.5)
                return 0;
            else
                return 1;
        }

        // function to compute the accuracy of the
        // raining and testing dataset
        public double Accuracy(double[][] trainData, double[] weights)
        {
            int numCorrect = 0;
            int numWrong = 0;
            int yIndex = trainData[0].Length - 1;
            for (int i = 0; i < trainData.Length; ++i)
            {
                double computed = ComputeDependent(trainData[i], weights); // implicit cast
                double desired = trainData[i][yIndex]; // 0.0 or 1.0

                double epsilon = 0.0000000001;
                if (Math.Abs(computed - desired) < epsilon)
                    ++numCorrect;
                else
                    ++numWrong;
            }
            return (numCorrect * 1.0) / (numWrong + numCorrect);
        }

        // particle class, each particle will have positions measure of fitness,
        // new position and best found position which are all being used in training
        public class Particle // for PSO training
        {
            public double[] position; // equivalent to weights
            public double error; // measure of fitness
            public double[] velocity; // determines new position
            public double[] bestPosition; // best found by this Particle
            public double bestError;

            public Particle(double[] position, double error, double[] velocity,
            double[] bestPosition, double bestError)
            {
                this.position = new double[position.Length];
                position.CopyTo(this.position, 0);
                this.error = error;
                this.velocity = new double[velocity.Length];
                velocity.CopyTo(this.velocity, 0);
                this.bestPosition = new double[bestPosition.Length];
                bestPosition.CopyTo(this.bestPosition, 0);
                this.bestError = bestError;
            }
        } // (nested) class Particle
    } // LogisticClassifier
}