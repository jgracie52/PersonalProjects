import java.io.*;
import java.util.*;
import java.text.*;

public class Display
{
  public static void displayRunData()
  {
    System.out.println("Run\tBest\tAverage\t\tDeviation");
    for(int i = 0; i < Input.runs; i++)
    {
      System.out.println(i + "\t" + Statistics.bestOfRun[i] + "\t" + Statistics.averageFitness[i] + "\t\t" + Statistics.deviationFitness[i]);
    }
  }

  public static void displayGenerationData()
  {
    System.out.println("Run\tGen\tBest\tBest Chromo\t\tAverage\t\tDeviation");
    for(int i = 0; i < Input.generations; i++)
    {
      System.out.println(GA.runs + "\t" + i + "\t" + Statistics.bestOfGen[i] + "\t" + Statistics.bestChromoOfGen[i] + "\t\t" + Statistics.averageFitnessGen[i] + "\t\t" + Statistics.deviationFitnessGen[i]);
    }
  }
}
