import java.io.*;
import java.util.*;
import java.math.*;

class Data
{
    public int dirtyBit;
    public long value;

    public Data(long value, int dirtyBit)
    {
      this.dirtyBit = dirtyBit;
      this.value = value;
    }
}


public class SIM
{
  // Using BigInteger for address buffer in case of potential overflow

  public char operation;
  public BigInteger address;
  public int numberReads;
  public int numberWrites;
  public double numberHits;
  public double numberMisses;


  public void LRUwrite(HashMap<Long, LinkedList<Data>> cache, long tag, long set, int associativity, int writeBack)
  {
    // Check all tags in cache
    if(cache.get(set) != null) // If set is found in cache
    {
      LinkedList<Data> list = cache.get(set);
      // Check the tags in the list
      for(int i = 0; i < list.size(); i++)
      {
        if(list.get(i).value == tag)
        {
          //System.out.println(list.get(i).value + ", " + tag);
          // Cache hit no need to write
          this.numberHits++;
          // Adjust LRU position
          list.remove(i);
          Data data = new Data(tag, 1);
          list.addLast(data);
          // if cache was hit must adjust dirtybit due to value change
          // adjust dirtyBit for writeBack purposes
          if(writeBack == 1)
          {
            list.getLast().dirtyBit = 1;
          }

          cache.put(set, list);
          return;
        }
      }
        // write miss requires look up in memory for new write block
        this.numberReads++;
        if(list.size() == associativity)
        {
          // Capacity miss
          this.numberMisses++;
          // if the value needs to be updated to mem
          if(list.get(0).dirtyBit == 1 && writeBack == 1)
          {
            this.numberWrites++;
          }

          // associativity determines whether or not the cache at this set is over filled
          // if assoc is less or equal to q size, replace the LRU
          // LRU should be the top value
          list.remove(0);
          Data data = new Data(tag, 0);
          list.add(data);
          list.getLast().value = tag;
        }
        else
        {
          // Place the value in empty slot
          // Cache at set is not over filled
          // No need to remove element from list
          // Cold miss
          this.numberMisses++;
          Data data = new Data(tag, 0);
          list.add(data);
          list.getLast().value = tag;
          // No need to adjust dirty bit. value has not changed

        }
      // place new list into the set location of the cache
      cache.put(set, list);
    }
    else // If set not found in cache
    {
      // Create new list and put into cache with the provided set
      // Cold miss
      this.numberMisses++;

      // cache miss requires look up for block in memory
      this.numberReads++;

      LinkedList<Data> newList = new LinkedList<>();
      Data data = new Data(tag, 0);
      newList.add(data);
      newList.getLast().value = tag;
      // no need to adjust dirty bit. value has not changed

      cache.put(set, newList);

    }
    return;
  }

  public void LRUread(HashMap<Long, LinkedList<Data>> cache, long tag, long set,  int associativity, int writeBack)
  {
    // LRU policy requires change to list position in event that
    // tag is found

    // Check if the set exits
    // If not increment number miss and return
    if(cache.get(set) != null)
    {
    // Pull the set list out and check for the tag in the list
    LinkedList<Data> list = new LinkedList<>();
    list = cache.get(set);
    for(int i = 0; i < list.size(); i++)
    {
      // Tag was found in set
      // Must also adjust tags current list position
      if(list.get(i).value == tag)
      {
        // Cache hit
        this.numberHits++;
        // Adjust LRU position
        int dirtyBit = list.get(i).dirtyBit;
        list.remove(i);
        Data data = new Data(tag, dirtyBit);
        list.add(data);

        cache.put(set, list);
        return;
      }
    }
    // Tag was not found in set
    // add value to the cache
    if(list.size() >= associativity)
    {

      // if the value needs to be updated to mem
      if(list.get(0).dirtyBit == 1 && writeBack == 1)
      {
        this.numberWrites++;
      }
      // associativity determines whether or not the cache at this set is over filled
      // if assoc is less or equal to q size, replace the LRU
      // LRU should be the top value
      list.remove(0);
      Data data = new Data(tag, 0);
      list.add(data);
      list.getLast().value = tag; // FLAG
      this.numberReads++;
      this.numberMisses++;

      // place new list into the set location of the cache
      cache.put(set, list);
      return;
    }
    else
    {
      // Place the value in empty slot
      // Cache at set is not over filled
      // No need to remove element from list
      this.numberReads++;
      this.numberMisses++;
      Data data = new Data(tag, 0);
      list.add(data);
      list.getLast().value = tag;
      // no need to adjust dirty bit. value has not changed

      // place new list into the set location of the cache
      cache.put(set, list);
      return;
    }
  }

 // If set not found in cache

    // Create new list and put into cache with the provided set
    LinkedList<Data> newList = new LinkedList<>();
    Data data = new Data(tag, 0);
    newList.add(data);
    newList.getLast().value = tag;
    this.numberReads++;
    this.numberMisses++;
    // no ndeed to adjust dirty bit. value has not changed

    cache.put(set, newList);
    return;
  }

  public void FIFOwrite(HashMap<Long, LinkedList<Data>> cache, long tag, long set, int associativity, int writeBack)
  {
    // Check all tags in cache
    if(cache.get(set) != null) // If set is found in cache
    {
      LinkedList<Data> list = cache.get(set);
      // Check the tags in the list
      for(int i = 0; i < list.size(); i++)
      {
        if(list.get(i).value == tag)
        {
          // Cache hit no need to write
          this.numberHits++;

          // if cache was hit must adjust dirtybit due to value change
          // adjust dirtyBit for writeBack purposes
          if(writeBack == 1)
          {
            list.get(i).dirtyBit = 1;
          }

          cache.put(set, list);
          return;
        }
      }
        // write miss requires look up in memory for new write block
        this.numberReads++;
        if(list.size() == associativity)
        {
          // Capacity miss
          this.numberMisses++;
          // if the value needs to be updated to mem
          if(list.get(0).dirtyBit == 1 && writeBack == 1)
          {
            this.numberWrites++;
          }

          // associativity determines whether or not the cache at this set is over filled
          // if assoc is less or equal to q size, replace the FI
          // FI should be the top value
          list.remove(0);
          Data data = new Data(tag, 0);
          list.add(data);
          list.getLast().value = tag;
        }
        else
        {
          // Place the value in empty slot
          // Cache at set is not over filled
          // No need to remove element from list
          // Cold miss
          this.numberMisses++;
          Data data = new Data(tag, 0);
          list.add(data);
          list.getLast().value = tag;
          // No need to adjust dirty bit. value has not changed

        }
      // place new list into the set location of the cache
      cache.put(set, list);
    }
    else // If set not found in cache
    {
      // Create new list and put into cache with the provided set
      // Cold miss
      this.numberMisses++;

      // cache miss requires look up for block in memory
      this.numberReads++;

      LinkedList<Data> newList = new LinkedList<>();
      Data data = new Data(tag, 0);
      newList.add(data);
      newList.getLast().value = tag;
      // no need to adjust dirty bit. value has not changed

      cache.put(set, newList);

    }
    return;
  }

  public void FIFOread(HashMap<Long, LinkedList<Data>> cache, long tag, long set, int associativity, int writeBack)
  {
    // FIFO policy does not require change to list position in event that
    // tag is found

    // Check if the set exits
    // If not increment number miss and return
    if(cache.get(set) != null)
    {
    // Pull the set list out and check for the tag in the list
    LinkedList<Data> list = new LinkedList<>();
    list = cache.get(set);
    for(int i = 0; i < list.size(); i++)
    {
      // Tag was found in set
      if(list.get(i).value == tag)
      {
        // Cache hit
        this.numberHits++;
        // No need to adjust position in cache
        return;
      }
    }
    // Tag was not found in set
    // add value to the cache
    if(list.size() >= associativity)
    {

      // if the value needs to be updated to mem
      if(list.get(0).dirtyBit == 1 && writeBack == 1)
      {
        //System.out.println("y tho");
        this.numberWrites++;
      }
      // associativity determines whether or not the cache at this set is over filled
      // if assoc is less or equal to q size, replace the FI
      // FI should be the top value
      list.remove(0);
      Data data = new Data(tag, 0);
      list.add(data);
      list.getLast().value = tag; // FLAG
      this.numberReads++;
      this.numberMisses++;

      // place new list into the set location of the cache
      cache.put(set, list);
      return;
    }
    else
    {
      // Place the value in empty slot
      // Cache at set is not over filled
      // No need to remove element from list
      this.numberReads++;
      this.numberMisses++;
      Data data = new Data(tag, 0);
      list.add(data);
      list.getLast().value = tag;
      // no need to adjust dirty bit. value has not changed

      // place new list into the set location of the cache
      cache.put(set, list);
      return;
    }
  }

 // If set not found in cache

    // Create new list and put into cache with the provided set
    LinkedList<Data> newList = new LinkedList<>();
    Data data = new Data(tag, 0);
    newList.add(data);
    newList.getLast().value = tag;
    this.numberReads++;
    this.numberMisses++;
    // no ndeed to adjust dirty bit. value has not changed

    cache.put(set, newList);
    return;
  }

  public int readLine(Scanner reader)
  {
    if(reader.hasNextLine())
    {
      // Read operation
      reader.useDelimiter("(?<=.)");
      String s = reader.next();
      this.operation = s.charAt(0);
      // Read buffer hex values

      reader.next();
      reader.next();
      reader.next();
      // Read hexstring and convert to long
      String hexString = reader.nextLine();
      this.address = new BigInteger(hexString, 16);

      return 0;
    }
    return -1;
  }

  public void printData()
  {
    double missRatio = 1 - (this.numberHits/(this.numberHits + this.numberMisses));
    System.out.println("Miss ratio: " + missRatio);
    //System.out.println("Number of Hits: " + this.numberHits);
    //System.out.println("Number of Misses: " + this.numberMisses);
    System.out.println("Number of writes: " + this.numberWrites);
    System.out.println("Number of reads : " + this.numberReads);

  }

  public void printCache(HashMap<Long, LinkedList<Data>> cache)
  {
    // Helper method to print final cache state
    for(HashMap.Entry<Long, LinkedList<Data>> entry : cache.entrySet())
    {
      LinkedList<Data> list = entry.getValue();
      System.out.println("Cache set: " + entry.getKey());
      for(int i = 0; i < list.size(); i++)
      {
        System.out.println("Cache value: " + list.get(i).value);
        System.out.println("Dirty bit: " + list.get(i).dirtyBit);
      }
    }
  }

  SIM(String [] args) throws IOException
  {
    if(args.length != 5)
    {
      System.out.println("<!>Did not enter proper arguments<!>");
      System.out.println("<!>java SIM <CACHE_SIZE> <ASSOC> <REPLACEMENT> <WB> <TRACE_FILE><!>");
      return;
    }

    int cacheSize = Integer.parseInt(args[0]);
    int associativity = Integer.parseInt(args[1]);
    int replacementType = Integer.parseInt(args[2]);
    int writeBack = Integer.parseInt(args[3]);
    String fname = args[4];
    // Block size is always 64Bytes
    int blockSize = 64;
    // Address size is 64 bits

    int numberOfSets = (int)(Math.log(cacheSize/(blockSize * associativity))/Math.log(2)); // set bits
    int tagOp = numberOfSets + 6;
    HashMap<Long, LinkedList<Data>> cache = new HashMap<>();
    File file = new File(fname);
    Scanner reader = new Scanner(file);

    long tag;
    long set;

    if(replacementType == 1) // FIFO
    {
      // Run the FIFO simulation
      while(readLine(reader) != -1)
      {

        tag = this.address.shiftRight(12).longValue();
        set = this.address.shiftRight(6).longValue();
        set = set << (64 - numberOfSets);
        set = set >>> (64 - numberOfSets);

        if(this.operation == 'R')
        {
          FIFOread(cache, tag, set, associativity, writeBack);
          this.numberReads++;
        }
        else if(this.operation == 'W')
        {
          FIFOwrite(cache, tag, set, associativity, writeBack);
          //this.numberWrites++;
          if(writeBack == 0)
          {
            this.numberWrites++;
          }
        }
        else
        {
          System.out.println("<!>Wrong operation was read<!>");
        }

      }

    }
    else if(replacementType == 0) // LRU
    {
      // Run the LRU simulation
      while(readLine(reader) != -1)
      {
        tag = this.address.shiftRight(12).longValue();
        set = this.address.shiftRight(6).longValue();
        set = set << (64 - numberOfSets);
        set = set >>> (64 - numberOfSets);

        if(this.operation == 'R')
        {
          LRUread(cache, tag, set, associativity, writeBack);
        }
        else if(this.operation == 'W')
        {
          LRUwrite(cache, tag, set, associativity, writeBack);
          if(writeBack == 0)
          {
            this.numberWrites++;
          }
        }
        else
        {
          System.out.println("<!>Wrong operation was read<!>");
        }
      }
    }
    else
    {
      System.out.println("<!>Incorrect argument type<!>");
      System.out.println("<!>Argument must be of type 1 or 0<!>");
    }

    printData();
    //printCache(cache);

  }
  public static void main(String [] args) throws IOException
  {
    new SIM(args);
  }
}
