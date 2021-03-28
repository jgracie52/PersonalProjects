import java.util.*;
import java.io.*;

public class GA
{


  /************************************
  * Global Variables                  *
  *************************************/
  public static FitnessFunction problem;
  public static Input input;

  public static Representation[] population;
  public static Representation[] children;

  public static int generations;
  public static int runs;

  public static Statistics stats;

  public static Random r;
  /***********************************
  * Static Classes                   *
  ***********************************/
  public static void main(String[] args) throws java.io.IOException
  {
    //Local Variables

    //Set up GA
    problem = new OneMax();
    input = new Input(args[0]);
    stats = new Statistics();
    r = new Random();

    // Number of GA Runs
    for(runs = 0; runs < Input.runs; runs++)
    {
      // Number of GA Generations per Run
      population = new Representation[Input.popSize];
      children = new Representation[Input.popSize];

      //Initialize the population and children arrays
      for(int i = 0; i < Input.popSize; i++)
      {
        population[i] = new Representation();
        children[i] = new Representation();
      }

      for(generations = 0; generations < Input.generations; generations++)
      {
        //Determine fitness of each pop member
          for(int i = 0; i < input.popSize; i++)
          {
            problem.calculateFitness(population[i]);
          }
        //Run selection crossover loop
          Representation parent1;
          Representation parent2;
          for(int i = 0; i < input.popSize; i = i + 2)
          {
            // Select the parents for crossover
            parent1 = Operators.selectParent();
					  parent2 = parent1;
					  while (parent2 == parent1)
            {
						  parent2 = Operators.selectParent();
					  }

            // Run crossover
            if(r.nextDouble() < Input.crossOverRate)
              Operators.crossOver(parent1, parent2, children[i], children[i+1]);
            else
              Operators.noCrossOver(parent1, parent2, children[i], children[i+1]);
          }
        //Run mutation
          for(int i = 0; i < input.popSize; i++)
          {
            Operators.mutate(population[i]);
          }
        //Copy children to parent array
         for(int i = 0; i < input.popSize; i++)
         {
           Operators.copyOver(children[i], population[i]);
         }

         Statistics.generateGenStatisticValues();
        //loop again
      }

      //Finalize statistics for current run
      Statistics.generateRunStatisticValues();

      //TODO
      //Output the data for this run
      Display.displayGenerationData();
    }
    Display.displayRunData();
  }

}
