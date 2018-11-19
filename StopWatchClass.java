package edu.nyu.pqs.stopwatch.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * A thread-safe object that can be used for timing laps. The stopwatch objects are created in the
 * StopwatchFactory. Different threads can share a single stopwatch object and safely call any of
 * the stopwatch methods.
 *
 */
public class StopWatchClass implements Stopwatch {
  private String id;
  private boolean running;
  private long start;
  private long lastlap;
  private List<Long> laptimes;
  private final Object lock_running = new Object();
  private final Object lock_laptimes = new Object();

  public StopWatchClass(String id) {
    this.id = id;
    running = false;
    start = 0;
    lastlap = 0;
    laptimes = new ArrayList<Long>();
  }

  /**
   * Returns the Id of this stopwatch
   * 
   * @return the Id of this stopwatch. Will never be empty or null.
   */
  @Override
  public String getId() {
    return id;
  }

  /**
   * Starts the stopwatch.
   * 
   * @throws IllegalStateException thrown when the stopwatch is already running
   */
  @Override
  public void start() {
    synchronized (lock_running) {
      if (running)
        throw new IllegalStateException();
      this.running = true;
      start = new Date().getTime();
      lastlap = start;
    }
  }

  /**
   * Stores the time elapsed since the last time lap() was called or since start() was called if
   * this is the first lap.
   * 
   * @throws IllegalStateException thrown when the stopwatch isn't running
   */
  @Override
  public void lap() {

    synchronized (lock_running) {
      if (!running)
        throw new IllegalStateException();
      long currentTime = new Date().getTime();
      long laptime = currentTime - lastlap;
      lastlap = currentTime;
      synchronized (lock_laptimes) {
        laptimes.add(laptime);
      }
    }
  }

  /**
   * Stops the stopwatch (and records one final lap).
   * 
   * @throws IllegalStateException thrown when the stopwatch isn't running
   */
  @Override
  public void stop() {
    synchronized (lock_running) {
      if (!running)
        throw new IllegalStateException();
      long currentTime = new Date().getTime();
      long laptime = currentTime - lastlap;
      lastlap = currentTime;
      running = false;
      synchronized (lock_laptimes) {
        laptimes.add(laptime);
      }
    }
  }

  /**
   * Resets the stopwatch. If the stopwatch is running, this method stops the watch and resets it.
   * This also clears all recorded laps.
   */
  @Override
  public void reset() {
    synchronized (lock_running) {
      running = false;
      start = 0;
      lastlap = 0;
      synchronized (lock_laptimes) {
        laptimes = new ArrayList<Long>();
      }
    }
  }

  /**
   * Returns a list of lap times (in milliseconds). This method can be called at any time and will
   * not throw an exception.
   * 
   * @return a list of recorded lap times or an empty list.
   */
  @Override
  public List<Long> getLapTimes() {
    synchronized (lock_laptimes) {
      return laptimes;
    }
  }

}
