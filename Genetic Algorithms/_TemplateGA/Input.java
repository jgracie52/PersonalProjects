import java.io.*;
import java.util.*;
import java.text.*;

public class Input
{
    public static String problem;
    public static int popSize;
    public static int geneStringSize;
    public static int generations;
    public static int runs;

    public static int mutationStyle;
    public static int crossOverStyle;
    public static int selectionStyle;

    public static double mutationRate;
    public static double crossOverRate;
    public static int tournamentSize;

  	public Input(String inputFileName) throws java.io.IOException
    {
      String readLine;
		  BufferedReader fileInput = new BufferedReader(new FileReader (inputFileName));

      problem = fileInput.readLine().substring(30);
      popSize = Integer.parseInt(fileInput.readLine().substring(30).trim());
      geneStringSize = Integer.parseInt(fileInput.readLine().substring(30).trim());
  		generations = Integer.parseInt(fileInput.readLine().substring(30).trim());
  		runs = Integer.parseInt(fileInput.readLine().substring(30).trim());

      mutationStyle = Integer.parseInt(fileInput.readLine().substring(30).trim());
      crossOverStyle = Integer.parseInt(fileInput.readLine().substring(30).trim());
      selectionStyle = Integer.parseInt(fileInput.readLine().substring(30).trim());

      mutationRate = Double.parseDouble(fileInput.readLine().substring(30).trim());
      crossOverRate = Double.parseDouble(fileInput.readLine().substring(30).trim());
      tournamentSize = Integer.parseInt(fileInput.readLine().substring(30).trim());
    }
}
