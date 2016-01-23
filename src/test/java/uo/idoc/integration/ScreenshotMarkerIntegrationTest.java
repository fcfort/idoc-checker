package uo.idoc.integration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

import uo.idoc.ScreenshotMarker;
import uo.idoc.difference.HouseDifference;
import uo.idoc.model.House;
import uo.idoc.model.HouseParser;

public class ScreenshotMarkerIntegrationTest {

  private final HouseParser hp = new HouseParser();
  private final ScreenshotMarker marker = new ScreenshotMarker();
  private static final String IMAGE_PATH = "YOUR_DIR/test.png";

  public void screenshotMarkerIntegrationTest() throws IOException {
    InputStream in = this.getClass().getClassLoader().getResourceAsStream("new");
    String content = CharStreams.toString(new InputStreamReader(in, Charsets.UTF_8));
    System.out.println(content);
    Closeables.closeQuietly(in);
    List<House> houses = hp.parse(content);
    System.out.println("Got house size " + houses.size());
    
    // houses
    //HouseDifference diff = new HouseDifference(Collections.<House> emptyList(), houses);
    HouseDifference diff = new HouseDifference(houses, Collections.<House> emptyList());
    System.out.println("Got new diff size " + diff.getNew().size());
    BufferedImage im = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("diff.png"));

    BufferedImage out = marker.markScreenshot(im, diff);
    
    ImageIO.write(out, "png", new File(IMAGE_PATH));
  }

}
