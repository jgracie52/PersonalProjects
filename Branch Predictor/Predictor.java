// Joshua Gracie
// PID: 4152915
// EEL4768

import java.util.*;
import java.io.*;
import java.math.*;

public class Predictor
{
  private static long[] cache;
  private static int[] GHB;
  private static long address;
  private static int index;
  private static char outcome;
  private static int numFails;
  private static int numTotal;
  private static int numSuccess;

  private static void runPrediction(int index, int N)
  {
    // Run the prediction
    // First give the outcome char an int type to use for comparison
    int outcomeVal = (outcome == 't') ? 1 : 0;

    // Increment the total number of increments
    numTotal++;

    // Check if the value in cache is equal to the outcome of the branch
    if(outcomeVal == 1 && (cache[index] == 2 || cache[index] == 3))
    {
      // This is reached when both vals are True
      // Must now adjust values accordingly
      cache[index]++;
      if(cache[index] > 3)
      {
        cache[index] = 3;
      }

      if(N != 0)
      {
        for(int i = GHB.length - 1; i > 0; i--)
        {
          GHB[i] = GHB[i -1];
        }
        GHB[0] = 1;
      }

      // Increment correct prediction counter
      numSuccess++;
    }
    else if(outcomeVal == 0 && (cache[index] == 1 || cache[index] == 0))
    {
      // This is reached when both vals are False
      cache[index]--;
      if(cache[index] < 0)
      {
        cache[index] = 0;
      }
      if(N != 0)
      {
        for(int i = GHB.length - 1; i > 0; i--)
        {
          GHB[i] = GHB[i - 1];
        }
        GHB[0] = 0;
      }

      // Increment correct prediction counter
      numSuccess++;
    }
    else
    {
      // This is reached when no values are the same
      if(N != 0)
      {
        for(int i = GHB.length - 1; i > 0; i--)
        {
          GHB[i] = GHB[i -1];
        }
      }

      // Must check what the outcome val is
      if(outcomeVal == 1)
      {
        cache[index]++;
        if(cache[index] > 3)
        {
          cache[index] = 3;
        }
        if(N != 0)
          GHB[0] = 1;
      }
      else
      {
        cache[index]--;
        if(cache[index] < 0)
        {
          cache[index] = 0;
        }
        if(N != 0)
          GHB[0] = 0;
      }

      // Increment failed prediction counter
      numFails++;
    }

  }

  private static int parseFile(Scanner reader) throws IOException
  {
    if(reader.hasNext())
    {
      // Read hexstring and convert to long
      String hexString = reader.next();
      address = Long.parseLong(hexString, 16);

      // Grab the outcome to use in prediction comparison
      outcome = reader.next().charAt(0);

      return 0;
    }
    return -1;
  }

  private static void runShifts(int M)
  {
    // Shift address around to get proper bits
    address = address >> 2;
    address = address << (64 - M);
    address = address >>> (64 - M);
  }

  private static void printStatistics(int M, int N)
  {
    // Print out prediction ratio (failures to total)
    double missprediction = ((double)numFails / (double)numTotal);
    System.out.println("<M>\t<N>\t<Missprediction Rate>");
    System.out.println(M + "\t" + N + "\t" + missprediction);
  }

  private static void setCache()
  {
    // This method is meant to set the cache to weakly taken
    // If it is not set prior, then java will default it to 0
    for(int i = 0; i < cache.length; i++)
    {
      // 2 is the value for weakly taken in this 2 bit predictor simulation
      cache[i] = 2;
    }
  }

  public static void main(String args[]) throws IOException
  {
    if(args.length != 3)
    {
      System.out.println("<!> Improper Arugments Passed <!>");
      System.out.println("<!> Arguments: java gshare <GPB> <RB> <Trace_File> <!>");
    }

    int M = Integer.parseInt(args[0]);
    int N = Integer.parseInt(args[1]);

    // Check put in place for N = 0
    // If it does end up as zero then the xOr will just xOr with zero
    // Since all values will be xOr'd by zero there is no logical change
    // to not xOring at all
    if(N != 0)
    {
      GHB = new int[N];
    }

    // Cache is 2^m in size
    cache = new long[(int)Math.pow(2, M)];

    File file = new File(args[2]);
    Scanner scanner = new Scanner(file);

    setCache();

    while(parseFile(scanner) != -1)
    {
      runShifts(M);
      long xOrVal;
      StringBuilder strNum = new StringBuilder();
      if(N != 0)
      {
        // Loop to get bits from GHB
        for(int e: GHB)
        {
          strNum.append(e);
        }
        xOrVal = Integer.parseInt(strNum.toString(), 2);
      }
      else
      {
        xOrVal = 0;
      }
      // Set xOrVal for finding index
      xOrVal = xOrVal << (M - N);
      int index = (int)(address ^ xOrVal);
      runPrediction(index, N);
    }

    printStatistics(M, N);

  }
}
