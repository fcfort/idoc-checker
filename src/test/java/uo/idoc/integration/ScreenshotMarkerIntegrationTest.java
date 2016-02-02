package uo.idoc.integration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import uo.idoc.ScreenshotMarker;
import uo.idoc.TestUtils;
import uo.idoc.difference.HouseDifference;

public class ScreenshotMarkerIntegrationTest {

  private final ScreenshotMarker marker = new ScreenshotMarker();
  private static final String IMAGE_PATH = "YOUR_DIR/test.png";

  @Test
  public void screenshotMarkerIntegrationTest() throws IOException {
    HouseDifference diff = new HouseDifference(TestUtils.readResourceAsHouses("hp"), TestUtils.readResourceAsHouses("hp_day2"));
    System.out.println("Got new diff size " + diff.getNew().size());
    BufferedImage im = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("diff.png"));

    BufferedImage out = marker.markScreenshot(im, diff);
    
    ImageIO.write(out, "png", new File(IMAGE_PATH));
  } 

}
