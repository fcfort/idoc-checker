package uo.idoc;

import java.io.IOException;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.common.collect.Lists;

public class IdocChecker {

  private static final int IMAGE_HEIGHT_PX = 4000;
  private static final int IMAGE_WIDTH_PX = 4000;
  private static final int CHECK_INTERVAL_SECONDS = 60;  
  
  @Parameter(names = "--username", description = "GMail username (no @gmail.com)")
  private String gmailUsername;

  @Parameter(names = "--password", description = "GMail password", password = true)
  private String gmailPassword;

  @Parameter(names = "--recipient", description = "Emails to send alerts to")
  private List<String> recipients;

  @Parameter(names = "--imageUrl", description = "URL to screencap")
  private String imageUrl;

  @Parameter(names = "--outputPath", description = "Screenshot diff location")
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
    new ScreenshotRunner(imageUrl, IMAGE_HEIGHT_PX, IMAGE_WIDTH_PX, CHECK_INTERVAL_SECONDS, workers).run();
  }
}
