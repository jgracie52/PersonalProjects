import java.io.*;
import java.util.*;
import java.text.*;

public class Statistics
{
  //Define global statistics variables
  public static double [] sumFitness;
  public static double [] sumFitnessSqaure;
  public static double [] averageFitness;
  public static double [] deviationFitness;
  public static double [] bestOfRun;

  public static double [] sumFitnessGen;
  public static double [] sumFitnessSqaureGen;
  public static double [] averageFitnessGen;
  public static double [] deviationFitnessGen;
  public static double [] bestOfGen;
  public static String [] bestChromoOfGen;

  public Statistics()
  {
    System.out.println("Setting up statistical calculations");
    sumFitness = new double[Input.runs];
    sumFitnessSqaure = new double[Input.runs];
    averageFitness = new double[Input.runs];
    deviationFitness = new double[Input.runs];
    bestOfRun = new double[Input.runs];

    sumFitnessGen = new double[Input.generations];
    sumFitnessSqaureGen = new double[Input.generations];
    averageFitnessGen = new double[Input.generations];
    deviationFitnessGen = new double[Input.generations];
    bestOfGen = new double[Input.generations];
    bestChromoOfGen = new String[Input.generations];
  }

  //Define statistical functions

  public static void generateGenStatisticValues()
  {
    averageFitnessGen[GA.generations] = sumFitnessGen[GA.generations] / (Input.popSize * Input.geneStringSize);

    // Standard Deviation
    deviationFitnessGen[GA.generations] = Math.sqrt(
							Math.abs(sumFitnessSqaureGen[GA.generations] - sumFitnessGen[GA.generations] * sumFitnessGen[GA.generations] / Input.popSize)
							/ (Input.popSize-1)
              );
  }

  public static void generateRunStatisticValues()
  {
    // Average
    averageFitness[GA.runs] = sumFitness[GA.runs] / (Input.generations * Input.popSize);

    // Standard Deviation
    deviationFitness[GA.runs] = Math.sqrt(
							Math.abs(sumFitnessSqaure[GA.runs] - sumFitness[GA.runs] * sumFitness[GA.runs] / (Input.popSize * Input.generations))
							/ ((Input.popSize * Input.generations)-1)
              );
  }
}
