package uo.idoc.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import uo.idoc.ScreenshotTaker;

public class ScreenshotMain {
  @Parameter(names = "--imageUrl", description = "URL to screenshot", required = true)
  private String imageUrl;

  @Parameter(names = "--imageHeight", description = "Image height in pixels to capture")
  private int imageHeightPx = 4200;

  @Parameter(names = "--imageWidth", description = "Image width in pixels to capture")
  private int imageWidthPx = 6500;

  @Parameter(names = "--tagName", description = "Tag name to capture")
  private String tagName = "svg";

  public static void main(String[] args) throws IOException, InterruptedException {
    ScreenshotMain main = new ScreenshotMain();
    new JCommander(main, args);
    main.run();
  }

  private void run() throws IOException {
    ScreenshotTaker t = new ScreenshotTaker(imageUrl, imageHeightPx, imageWidthPx, 30, tagName);
    BufferedImage i = t.takePartialScreenshot();
    ImageIO.write(i, "png", new File("C:/Users/Frank/Downloads/diff.png"));
  }
}
