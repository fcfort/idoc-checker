package uo.idoc.integration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import uo.idoc.ScreenshotMarker;
import uo.idoc.TestUtils;
import uo.idoc.difference.HouseDifference;
import uo.idoc.difference.StringDifference;
import uo.idoc.model.HouseParser;

public class HouseDiffIntegrationTest {

  private static final String IMAGE_PATH = "YOUR_DIR/test.png";

  public void textDiffIntegrationTest() throws IOException {
    // Diff text
    StringDifference s = new StringDifference(TestUtils.readResourceAsString("hp"),
        TestUtils.readResourceAsString("hp_day2"));
    // Parse into house difference
    HouseParser hp = new HouseParser();
    HouseDifference diff = new HouseDifference(hp.parse(s.getOldData()), hp.parse(s.getNewData()));

    // Mark image
    BufferedImage out = new ScreenshotMarker().markScreenshot(TestUtils.readRourceAsImage("diff.png"), diff);

    // Write it out
    ImageIO.write(out, "png", new File(IMAGE_PATH));
  }

}
