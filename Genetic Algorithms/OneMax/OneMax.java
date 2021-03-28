import java.io.*;
import java.util.*;
import java.text.*;

public class OneMax extends FitnessFunction
{
  public OneMax()
  {
    System.out.println("Running the One Max Problem");
  }

  public void calculateFitness(Representation popMember)
  {
    int fitnessScore = 0;
    //Add up points for however many 1s are in the geneString
    for(int i = 0; i < Input.geneStringSize; i++)
    {
      if(popMember.chromo.charAt(i) == '1')
        fitnessScore++;
    }

    popMember.fitness = fitnessScore;

    // Sum up fitness scores for later statistics
    Statistics.sumFitness[GA.runs] += fitnessScore;
    Statistics.sumFitnessSqaure[GA.runs] += Math.pow(fitnessScore, 2);
    if(Statistics.bestOfRun[GA.runs] < popMember.fitness)
      Statistics.bestOfRun[GA.runs] = popMember.fitness;

      Statistics.sumFitnessGen[GA.generations] += fitnessScore;
      Statistics.sumFitnessSqaureGen[GA.generations] += Math.pow(fitnessScore, 2);
      if(Statistics.bestOfGen[GA.generations] < popMember.fitness)
      {
        Statistics.bestOfGen[GA.generations] = popMember.fitness;
        Statistics.bestChromoOfGen[GA.generations] = popMember.chromo;
      }

  }
}
