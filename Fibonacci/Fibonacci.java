import java.util.*;

public class Fibonacci
{
  public static long Fib(int findNumber)
  {
    long[] hold = new long[2];
    long finalVal = 0;
    hold[0] = 0;
    hold[1] = 1;

    for(int i = 0; i <= findNumber; i++)
    {
      finalVal = hold[0]  + hold[1];
      hold[1] = hold[0];
      hold[0] = finalVal;
    }

    return finalVal;
  }
  public static void main(String args[])
  {
    int num = Integer.parseInt(args[0]);
    System.out.println(Fib(num));
  }
}
