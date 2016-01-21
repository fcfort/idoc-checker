package uo.idoc.runner;

import java.io.IOException;

import uo.idoc.HttpRequester;
import uo.idoc.difference.StringDifference;
import uo.idoc.worker.TextDiffWorker;

public class HttpRequestRunner implements Runnable {

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

  public void run() {
    while (true) {
      System.out.println("Reading URL before");
      String before = readUrl(url);
      System.out.println("Waiting " + sleepTimeMillis + " millis");
      sleep();
      System.out.println("Reading URL after");
      String after = readUrl(url);

      if (!before.equals(after)) {
        System.out.println("Found difference!");
        StringDifference diff = new StringDifference(before, after);
        for (TextDiffWorker worker : workers) {
          worker.run(diff);
        }
      }
    }
  }

  private String readUrl(String url) {
    try {
      return requester.get(url);
    } catch (IOException e1) {
      throw new RuntimeException("Error reading " + url, e1);
    }
  }

  private void sleep() {
    try {
      Thread.sleep(sleepTimeMillis);
    } catch (InterruptedException e) {
      throw new RuntimeException("Thread interrupted");
    }
  }
}
