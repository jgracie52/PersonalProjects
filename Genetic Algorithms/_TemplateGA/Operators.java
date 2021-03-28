import java.io.*;
import java.util.*;
import java.text.*;

public class Operators
{
  //Define the mutation fucntion
  public static void mutate(Representation popMember)
  {
    StringBuilder tempString = new StringBuilder(popMember.chromo);
    switch(Input.mutationStyle)
    {
      case 1: //Uniform
        for(int i = 0; i < Input.geneStringSize; i++)
        {
          if(GA.r.nextDouble() < Input.mutationRate)
          {
            if(popMember.chromo.charAt(i) == '0')
              tempString.setCharAt(i, '1');
            else
              tempString.setCharAt(i, '0');
          }
        }
        break;
      default:
        return;
    }
    popMember.chromo = tempString.toString();
  }

  //Define selection function
  public static Representation selectParent()
  {
    Representation member1 = GA.population[GA.r.nextInt(Input.popSize - 1)];
    Representation member2 = GA.population[GA.r.nextInt(Input.popSize - 1)];
    while(member2 == member1)
    {
      member2 = GA.population[GA.r.nextInt(Input.popSize - 1)];
    }

    switch(Input.selectionStyle)
    {
      case 1: //Tournament Selection
        return (member1.fitness > member2.fitness)? member1 : member2;
      case 2: //Proportional Selection
        break;
    }
    return member1;
  }

  //Define the crossover function
  public static void crossOver(Representation parent1, Representation parent2, Representation child1, Representation child2)
  {
    switch(Input.crossOverStyle)
    {
      case 1: //One-point CrossOver
        //Built for binary strings
        int crossOverPoint = GA.r.nextInt(Input.geneStringSize - 1);
        child1.chromo = parent1.chromo.substring(0,crossOverPoint) + parent2.chromo.substring(crossOverPoint);
        child2.chromo = parent2.chromo.substring(0, crossOverPoint) + parent1.chromo.substring(crossOverPoint);
        break;
      case 2: //Two-point CrossOver
        int crossOverPoint1 = GA.r.nextInt(Input.geneStringSize - 1);
        int crossOverPoint2 = GA.r.nextInt(Input.geneStringSize - 1);
        while(crossOverPoint2 < crossOverPoint1)
          crossOverPoint2 = GA.r.nextInt(Input.geneStringSize - 1);
        child1.chromo = parent1.chromo.substring(0, crossOverPoint1) + parent2.chromo.substring(crossOverPoint1, crossOverPoint2) + parent1.chromo.substring(crossOverPoint2);
        child2.chromo = parent2.chromo.substring(0, crossOverPoint1) + parent1.chromo.substring(crossOverPoint1, crossOverPoint2) + parent2.chromo.substring(crossOverPoint2);
        break;
      case 3: //Uniform CrossOver
        break;
      default:
        return;
    }
  }

  public static void noCrossOver(Representation parent1, Representation parent2, Representation child1, Representation child2)
  {
    child1.chromo = parent1.chromo;
    child2.chromo = parent2.chromo;
  }

  //Copy children array to population array
  public static void copyOver(Representation child, Representation parent)
  {
    parent.chromo = child.chromo;
    parent.fitness = child.fitness;

    //Add other representation data here
  }

  //
}
