

package musicclassificationbbygenre;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.DataSet;
import org.neuroph.core.learning.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TrainingSetImport;
import org.neuroph.util.TransferFunctionType;

public class MusicClassificationbByGenre {

  static int i = 0;

  public static double Round(double a) {

    i++;
    String s = Double.toString(a);
    if (i == 4) {
      i = 0;
    }

    String s1 = null;

    double d = 1;
    if (s.charAt(0) == '0') {
      s1 = s.substring(0, 5);
      d = Double.parseDouble(s1);
      if (d >= 0.9)
        return 1;
      else
        return 0;
    } else
      return 0;
  }

  public static void main(String[] args) {

    String trainingSetFileName = "Music.txt";
    String testingSetFileName = "MusicTest.txt";
    int inputsCount = 8;
    int outputsCount = 4;

    System.out.println("Running Sample");
    System.out.println("Using training set " + trainingSetFileName);

    // create training set and testing set
    DataSet trainingSet = null;
    DataSet testingSet = null;

    try {
      trainingSet = TrainingSetImport.importFromFile(trainingSetFileName, inputsCount, outputsCount, ",");
    } catch (FileNotFoundException ex) {
      System.out.println("File not found!");
    } catch (IOException | NumberFormatException ex) {
      System.out.println("Error reading file or bad number format!");
    }

    // create multi layer perceptron
    System.out.println("Creating neural network");
    MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 8, 20, 4);

    // set learning parameters
    MomentumBackpropagation learningRule = (MomentumBackpropagation) neuralNet.getLearningRule();
    learningRule.setLearningRate(0.5);
    learningRule.setMomentum(0.8);

    // learn the training set
    System.out.println("Training neural network...");
    neuralNet.learn(trainingSet);
    System.out.println("Done!");

    try {
      testingSet = TrainingSetImport.importFromFile(testingSetFileName, 8, 0, ",");
    } catch (FileNotFoundException ex) {
      System.out.println("File not found!");
    } catch (IOException | NumberFormatException ex) {
      System.out.println("Error reading file or bad number format!");
    }

    // test perceptron
    System.out.println("Testing trained neural network");
    testMusicClassification(neuralNet, testingSet);
  }

  public static void testMusicClassification(NeuralNetwork nnet, DataSet dset) {

    for (DataSetRow trainingElement : dset.getRows()) {
      nnet.setInput(trainingElement.getInput());
//      System.out.println(Arrays.toString(trainingElement.getInput()));
      nnet.calculate();
      double[] networkOutput = nnet.getOutput();
      System.out.println(Arrays.toString(nnet.getOutput()));
      for (int i = 0; i < networkOutput.length; i++) {
        networkOutput[i] = Round(networkOutput[i]);
      }
      System.out.print("Input: " + Arrays.toString(trainingElement.getInput()));

      System.out.print(" Output: " + Arrays.toString(networkOutput));
      if (networkOutput[0] == 1)
        System.out.println(" rock");
      if (networkOutput[1] == 1)
        System.out.println(" classic");
      if (networkOutput[2] == 1)
        System.out.println(" jazz");
      if (networkOutput[3] == 1)
        System.out.println(" folk");
      System.out.println();
    }
  }
}
