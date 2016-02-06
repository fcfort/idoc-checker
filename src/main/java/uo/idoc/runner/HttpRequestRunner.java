package uo.idoc.runner;

import java.io.IOException;

import uo.idoc.HttpRequester;
import uo.idoc.difference.StringDifference;
import uo.idoc.worker.TextDiffWorker;

public class HttpRequestRunner implements Runnable {

  private static final int RETRY_SLEEP_TIME_MILLIS = 10_000;
  
  private final HttpRequester requester;
  private final String url;  
  private final int sleepTimeMillis;
  private final Iterable<TextDiffWorker> workers;

  public HttpRequestRunner(String url, Iterable<TextDiffWorker> workers, int sleepTimeMillis) {
    requester = new HttpRequester();
    this.url = url;
    this.sleepTimeMillis = sleepTimeMillis;
    this.workers = workers;
  }

  /**
   * If either request (before or after) throws an exception then retry until it
   * succeeds. This way failures to read the request won't result in a diff.
   */
  public void run() {
    while (true) {
      System.out.println("Reading URL before");
      String before = getUrlWithRetries(url);
      System.out.println("Waiting " + sleepTimeMillis + " millis");
      sleep(sleepTimeMillis);
      System.out.println("Reading URL after");
      String after = getUrlWithRetries(url);
      
      if (!before.equals(after)) {
        System.out.println("Found difference!");
        StringDifference diff = new StringDifference(before, after);
        for (TextDiffWorker worker : workers) {
          worker.run(diff);
        }
      }
    }
  }  

  private String getUrlWithRetries(String url) {
    try {
      return requester.get(url);
    } catch (IOException e) {
      System.out.println("Caught error, sleeping for " + RETRY_SLEEP_TIME_MILLIS);
      sleep(RETRY_SLEEP_TIME_MILLIS);
      return getUrlWithRetries(url);
    }
  }
  
  private void sleep(int sleepTimeMillis) {
    try {
      Thread.sleep(sleepTimeMillis);
    } catch (InterruptedException e) {
      throw new RuntimeException("Thread interrupted");
    }
  }
}
