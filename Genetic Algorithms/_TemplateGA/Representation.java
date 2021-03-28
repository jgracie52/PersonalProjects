import java.io.*;
import java.util.*;
import java.text.*;

public class Representation
{
  public String chromo;
  public double fitness;
  public double scaledFitness;

  public Representation()
  {
    //Generate the inital pop based on Representation style
    this.chromo = "";
    for(int i = 0; i < Input.geneStringSize; i++)
    {
      if(GA.r.nextDouble() < 0.5)
        this.chromo += '0';
      else
        this.chromo += '1';
    }
    //System.out.println(this.chromo);

    //Initialize the fitness values
    this.fitness = -1.0;
    this.scaledFitness = -1.0;
  }
}
