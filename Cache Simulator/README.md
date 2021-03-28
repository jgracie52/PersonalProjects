#Cache Simulator

This is my Cache Simulator project from my Computer Architecture class in Spring 2019.

The goal was to implement a simple cache simulator in Java using an LRU and a FIFO replacement policy.

java -jar SIM.jar <CACHE_SIZE> <ASSOC> <REPLACEMENT> <WB> <TRACE_FILE>
  1) CACHE_SIZE = Size of the cache, such as 8 for 8KB or 128 for 128KB.
  2) ASSOC = Associativity of the cache, such as 4.
  3) REPLACEMENT = Either Least Recently Used (LRU) or First in First Out (FIFO)
  4) WB = Either Write-Back or Write-Through
  5) TRACE_FILE = The file with the commands for the cache simulator
