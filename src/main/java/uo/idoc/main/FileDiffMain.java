package uo.idoc.main;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.common.collect.Lists;

import uo.idoc.ScreenshotTaker;
import uo.idoc.email.Emailer;
import uo.idoc.email.GmailEmailer;
import uo.idoc.imgur.ImgurUploader;
import uo.idoc.imgur.LargeImageUploader;
import uo.idoc.runner.HttpRequestRunner;
import uo.idoc.worker.ImgurWorker;
import uo.idoc.worker.StringDiffPrinter;
import uo.idoc.worker.TextDiffWorker;

public class FileDiffMain {
  private static final int ONE_MINUTE_MILLIS = 60 * 1000;
  private static final int FIVE_MINUTES_MILLIS = 5 * 60 * 1000;
  private static final int THIRTY_MINUTES_MILLIS = 30 * 60 * 1000;
  private static final int ONE_HOUR_MILLIS = 60 * 60 * 1000;

  @Parameter(names = "--username", description = "GMail username (no @gmail.com)", required = true)
  private String gmailUsername;

  @Parameter(names = "--password", description = "GMail password", password = true, required = true)
  private String gmailPassword;

  @Parameter(names = "--recipient", description = "Emails to send alerts to", required = true)
  private List<String> recipients;

  @Parameter(names = "--fileUrl", description = "URL to GET request", required = true)
  private String fileUrl;

  @Parameter(names = "--imageUrl", description = "URL to screenshot", required = true)
  private String imageUrl;

  @Parameter(names = "--imageHeight", description = "Image height in pixels to capture")
  private int imageHeightPx = 4200;

  @Parameter(names = "--imageWidth", description = "Image width in pixels to capture")
  private int imageWidthPx = 6500;

  @Parameter(names = "--tagName", description = "Tag name to capture")
  private String tagName = "svg";

  @Parameter(names = "--outputPath", description = "Where to put the diff", required = true)
  private String outputPath;
  
  @Parameter(names = "--imgurClientId", description = "imgur API Client ID", required = true)
  private String imgurClientId;
    
  public static void main(String[] args) throws IOException, InterruptedException {
    FileDiffMain main = new FileDiffMain();
    new JCommander(main, args);
    main.run();
  }

  private void run() throws InterruptedException, IOException {
    List<Integer> checkIntervalsMillis = Lists.newArrayList(FIVE_MINUTES_MILLIS,
        THIRTY_MINUTES_MILLIS);
    ExecutorService e = Executors.newFixedThreadPool(checkIntervalsMillis.size());

    GmailEmailer gmailer = new GmailEmailer(gmailUsername, gmailPassword);    
    ScreenshotTaker screenshotTaker = new ScreenshotTaker(imageUrl, imageHeightPx, imageWidthPx, 30, tagName);
        
    ImgurWorker imgurWorker = new ImgurWorker(
        new LargeImageUploader(new ImgurUploader(imgurClientId)), 
        screenshotTaker, 
        new Emailer(gmailer, recipients));
    
    List<TextDiffWorker> workers = Lists.newArrayList(
        new StringDiffPrinter(),
        imgurWorker);

    for (int millis : checkIntervalsMillis) {
      e.submit(new HttpRequestRunner(fileUrl, workers, millis));
      Thread.sleep(1000);
    }

    e.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
  }

}
