package uo.idoc.main;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.common.collect.Lists;

import uo.idoc.runner.HttpRequestRunner;
import uo.idoc.worker.StringDiffPrinter;
import uo.idoc.worker.TextDiffWorker;

public class FileDiffMain {
  private static final int THIRTY_SECONDS_MILLIS = 30 * 1000;
  private static final int ONE_MINUTE_MILLIS = 60 * 1000;
  private static final int FIVE_MINUTES_MILLIS = 5 * 60 * 1000;
  private static final int THIRTY_MINUTES_MILLIS = 30 * 60 * 1000;
  private static final int ONE_HOUR_MILLIS = 60 * 60 * 1000;

  @Parameter(names = "--fileUrl", description = "URL to GET request", required = true)
  private String fileUrl;

  public static void main(String[] args) throws IOException, InterruptedException {
    FileDiffMain main = new FileDiffMain();
    new JCommander(main, args);
    main.run();
  }

  private void run() throws InterruptedException {
    List<Integer> checkIntervalsMillis = Lists.newArrayList(THIRTY_SECONDS_MILLIS, ONE_MINUTE_MILLIS,
        FIVE_MINUTES_MILLIS, THIRTY_MINUTES_MILLIS, ONE_HOUR_MILLIS);
    ExecutorService e = Executors.newFixedThreadPool(checkIntervalsMillis.size());

    for (int millis : checkIntervalsMillis) {
      e.submit(new HttpRequestRunner(fileUrl, Lists.<TextDiffWorker> newArrayList(new StringDiffPrinter()), millis));
      Thread.sleep(1000);
    }

    e.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
  }

}
