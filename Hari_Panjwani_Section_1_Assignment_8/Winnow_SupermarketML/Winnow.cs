using System;
  
  public class Winnow
  {
    private int numInput;
    private double[] weights; 
    private double threshold; // to determine Y = 0 or 1
    private double alpha; // increase/decrase factor
    private static Random rnd;

    // setting the initial weights to 1
    // setting the threshold to number of features i.e., 72/2
    // setting the alpha value as 2
    public Winnow(int numInput, int rndSeed)
    {
      this.numInput = numInput;
      this.weights = new double[numInput];
      for (int i = 0; i < weights.Length; ++i)
        //weights[i] = numInput / 2.0;
        weights[i] = 1;
      this.threshold = (1.0 * numInput) / 2;
      this.alpha = 2.0;
      rnd = new Random(rndSeed);
    }

    // this function is to predict the value of dependent variable,
    // what it does is sum up the weights of all the feature whose value is 1
    // if sum is greater than threshold, it returns 1, else it return 0 
    public int ComputeY(int[] xValues)
    {
      double sum = 0.0;
      for (int i = 0; i < numInput; ++i)
        sum += weights[i] * xValues[i];
      if (sum > this.threshold)
        return 1;
      else
        return 0;
    }


    /*
      Winnow update function, If the current model predicts the output correctly, don’t change anything. 
      If it predicts true but should predict false, it is over-shooting, so weights that were used in the 
      prediction (i.e. the weights attached to active features) are reduced i.e., divide the weights of every 
      feature by alpha. Conversely, if the prediction is false but the correct result should be true, the active 
      features are not used enough to reach the threshold, so they should be bumped up i.e., multiply the weights 
      of every feature by alpha.
    */
    public double[] TrainWeights(int[][] trainData)
    {
      int[] xValues = new int[numInput];
      int target;
      int computed;
      ShuffleObservations(trainData);
      for (int i = 0; i < trainData.Length; ++i)
      {
        Array.Copy(trainData[i], xValues, numInput); // get the inputs
        target = trainData[i][numInput]; // last value is target
        computed = ComputeY(xValues);

        if (computed == 1 && target == 0) // need to decrease weights
        {
          for (int j = 0; j < numInput; ++j)
          {
            if (xValues[j] == 0) continue; // no change when xi = 0
            weights[j] = weights[j] / alpha; // demotion
          }
        }
        else if (computed == 0 && target == 1) // need to increase weights
        {
          for (int j = 0; j < numInput; ++j)
          {
            if (xValues[j] == 0) continue; // no change when xi = 0
            weights[j] = weights[j] * alpha; // promotion
          }
        }
      } // each training item

      double[] result = new double[numInput]; // = number weights
      Array.Copy(this.weights, result, numInput);
      return result;
    } // Train

    // We are shuffling the trainData, so that while training the weights,
    // the data should come in ranom order, it uses Fisher-Yates shuffle algorithm
    private static void ShuffleObservations(int[][] trainData)
    {
      for (int i = 0; i < trainData.Length; ++i)
      {
        int r = rnd.Next(i, trainData.Length);
        int[] tmp = trainData[r];
        trainData[r] = trainData[i];
        trainData[i] = tmp;
      }
    }

    /* this function computes the accuracy of the dataset, based on
       the weights which we have computed, what it does is it applies 
       the algorithm to the dataset and try to predict the severity of the claim
       and then it compares computed value with the target value, later it gives the
       total correct divide by total number of observation
    */
    public double Accuracy(int[][] trainData)
    {
      int numCorrect = 0;
      int numWrong = 0;

      int[] xValues = new int[numInput];
      int target;
      int computed;

      for (int i = 0; i < trainData.Length; ++i)
      {
        Array.Copy(trainData[i], xValues, numInput); // get the inputs
        target = trainData[i][numInput]; // last value is target
        computed = ComputeY(xValues);

        if (computed == target)
          ++numCorrect;
        else
          ++numWrong;
      }
      return (numCorrect * 1.0) / (numCorrect + numWrong);
    }
  } // Winnow