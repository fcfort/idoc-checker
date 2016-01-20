package uo.idoc;

import java.io.IOException;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.common.collect.Lists;

public class IdocChecker {
  @Parameter(names = "--checkInterval", description = "How often to check website in seconds")
  private int checkIntervalSeconds = 60;  
  
  @Parameter(names = "--imageHeight", description = "Image height in pixels to capture")
  private int imageHeightPx = 4000;

  @Parameter(names = "--imageWidth", description = "Image width in pixels to capture")
  private int imageWidthPx = 6000;  
  
  @Parameter(names = "--username", description = "GMail username (no @gmail.com)", required = true)
  private String gmailUsername;

  @Parameter(names = "--password", description = "GMail password", password = true, required = true)
  private String gmailPassword;

  @Parameter(names = "--recipient", description = "Emails to send alerts to", required = true)
  private List<String> recipients;

  @Parameter(names = "--imageUrl", description = "URL to screencap", required = true)
  private String imageUrl;

  @Parameter(names = "--outputPath", description = "Screenshot diff location", required = true)
  private String outputPath;

  public static void main(String... args) throws IOException {
    IdocChecker main = new IdocChecker();
    new JCommander(main, args);
    main.run();
  }

  public void run() throws IOException {
    ImageDiffWriter checker = new ImageDiffWriter(outputPath);
    ImageDiffEmailer emailer = new ImageDiffEmailer(new GmailEmailer(gmailUsername, gmailPassword), recipients);

    List<DiffWorker> workers = Lists.<DiffWorker> newArrayList(checker, emailer);
    new ScreenshotRunner(imageUrl, imageHeightPx, imageWidthPx, checkIntervalSeconds, new SimpleByteDiffer(),
        workers).run();
  }
}
