package uo.idoc.worker;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import uo.idoc.ScreenshotMarker;
import uo.idoc.ScreenshotTaker;
import uo.idoc.difference.HouseDifference;
import uo.idoc.difference.StringDifference;
import uo.idoc.email.Emailer;
import uo.idoc.imgur.LargeImageUploader;
import uo.idoc.model.House;
import uo.idoc.model.HouseParser;

public class ImgurWorker implements TextDiffWorker {

  private static final HouseParser HOUSE_PARSER = new HouseParser();

  private final LargeImageUploader uploader;
  private final ScreenshotTaker screenshotTaker;
  private final ScreenshotMarker screenshotMarker = new ScreenshotMarker();
  private final Emailer emailer;

  public ImgurWorker(LargeImageUploader uploader, ScreenshotTaker screenshotTaker, Emailer emailer) {
    this.uploader = uploader;
    this.screenshotTaker = screenshotTaker;
    this.emailer = emailer;
  }

  @Override
  public void run(StringDifference diff) {
    List<House> oldHouses = HOUSE_PARSER.parse(diff.getOldData());
    List<House> newHouses = HOUSE_PARSER.parse(diff.getNewData());
    HouseDifference houseDiff = new HouseDifference(oldHouses, newHouses);

    Date now = new Date();
    String timestampString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(now);
    String title = "IDOCs for " + timestampString;
    
    try {
      BufferedImage markedImage = screenshotMarker.markScreenshot(
          screenshotTaker.takePartialScreenshot(), houseDiff);
      String link = uploader.uploadImage(markedImage, title);
      emailer.sendEmail(title, link);
      System.out.println(link);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
