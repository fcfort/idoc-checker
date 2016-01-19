package asdff;

import java.io.IOException;

public class IdocChecker {  
  private static final int IMAGE_HEIGHT_PX = 4000;
  private static final int IMAGE_WIDTH_PX = 4000;
  private static final int CHECK_INTERVAL_SECONDS = 60;
  
  public static void main(String... args) throws IOException {
    String outputPath = args[0];
    String imageUrl = args[1];
    
    ImageDiffWriter checker = new ImageDiffWriter(outputPath);

    new ScreenshotRunner(
        imageUrl, IMAGE_HEIGHT_PX, IMAGE_WIDTH_PX, CHECK_INTERVAL_SECONDS, checker.doWork()).run();
  }
}
