package uo.idoc.worker;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import uo.idoc.ScreenshotMarker;
import uo.idoc.ScreenshotTaker;
import uo.idoc.difference.HouseDifference;
import uo.idoc.difference.StringDifference;
import uo.idoc.model.House;
import uo.idoc.model.HouseParser;

public class ScreenMarkingWorker implements TextDiffWorker {

  private final HouseParser houseParser = new HouseParser();
  private final ScreenshotTaker screenshotTaker;
  private final ScreenshotMarker screenshotMarker = new ScreenshotMarker();
  private final Path outputPath;

  public ScreenMarkingWorker(ScreenshotTaker screenshotTaker, Path outputPath) {
    this.screenshotTaker = screenshotTaker;
    this.outputPath = outputPath;
  }

  @Override
  public void run(StringDifference diff) {
    List<House> oldHouses = houseParser.parse(diff.getOldData());
    List<House> newHouses = houseParser.parse(diff.getNewData());
    HouseDifference houseDiff = new HouseDifference(oldHouses, newHouses);
    BufferedImage im;
    try {
      im = screenshotTaker.takePartialScreenshot();
      BufferedImage out = screenshotMarker.markScreenshot(im, houseDiff);
      ImageIO.write(out, "png",
          new File(outputPath.toFile(), new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".png"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
