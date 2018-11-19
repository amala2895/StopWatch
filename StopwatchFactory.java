package edu.nyu.pqs.stopwatch.impl;

import java.util.ArrayList;
import java.util.List;
import edu.nyu.pqs.stopwatch.api.StopWatchClass;
import edu.nyu.pqs.stopwatch.api.Stopwatch;

/**
 * The StopwatchFactory is a thread-safe factory class for Stopwatch objects. It maintains
 * references to all created Stopwatch objects and provides a convenient method for getting a list
 * of those objects.
 *
 */
public class StopwatchFactory {
  private static List<Stopwatch> listOfWatches = new ArrayList<Stopwatch>();
  private static final Object lock = new Object();

  /**
   * Creates and returns a new Stopwatch object
   * 
   * @param id The identifier of the new object
   * @return The new Stopwatch object
   * @throws IllegalArgumentException if <code>id</code> is empty, null, or already taken.
   */
  public static Stopwatch getStopwatch(String id) {
    if (id == null || id.isEmpty())
      throw new IllegalArgumentException("Id is empty or null");
    for (Stopwatch s : listOfWatches) {
      if (s.getId().equals(id))
        throw new IllegalArgumentException("Id is already taken");
    }

    Stopwatch newWatch = new StopWatchClass(id);
    synchronized (lock) {
      listOfWatches.add(newWatch);
    }
    return newWatch;
  }

  /**
   * Returns a list of all created stopwatches
   * 
   * @return a List of all creates Stopwatch objects. Returns an empty list if no Stopwatches have
   *         been created.
   */
  public static List<Stopwatch> getStopwatches() {
    synchronized (lock) {
      return listOfWatches;
    }
  }
}
