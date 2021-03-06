package uo.idoc.runner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;

import uo.idoc.ScreenshotDiffer;
import uo.idoc.difference.ByteArrayDifference;
import uo.idoc.worker.ImageDiffWorker;

public class ScreenshotRunner implements Runnable {
  private final String tempFileURI;
  private byte[] oldData;
  private final int intervalSeconds;

  private static File createIframeHtml(int height, int width, String url) throws IOException {
    String data = String.format(
        "<html><body><iframe style=\"height: %dpx; width: %dpx;\" src=\"%s\"></iframe><body></html>", height, width,
        url);

    // Write iframe html
    File tempFile = File.createTempFile("iframe", ".html");
    Files.write(tempFile.toPath(), data.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    return tempFile;
  }

  private final Iterable<ImageDiffWorker> workers;
  private final WebDriver driver;
  private final ScreenshotDiffer differ;

  public ScreenshotRunner(String url, int imageHeight, int imageWidth, int intervalSeconds, ScreenshotDiffer differ,
      Iterable<ImageDiffWorker> workers) throws IOException {
    File tempFile = createIframeHtml(imageHeight, imageWidth, url);
    this.intervalSeconds = intervalSeconds;
    tempFileURI = tempFile.toURI().toString();
    this.workers = workers;
    WebDriver ffDriver = new FirefoxDriver();
    driver = new Augmenter().augment(ffDriver);
    this.differ = differ;
  }

  public void run() {
    while (true) {
      // Launch driver
      driver.get(tempFileURI);

      // Wait for the page to load
      try {
        (new WebDriverWait(driver, intervalSeconds)).until(new Predicate<WebDriver>() {
          public boolean apply(WebDriver arg0) {
            return false;
          }
        });
      } catch (TimeoutException e) {
      }

      // Generate diff
      if (oldData == null) {
        System.out.println("Taking initial screenshot at " + (new Date()).toString());
        oldData = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
      } else {
        System.out.println("Taking diff screenshot at " + (new Date()).toString());
        byte[] newData = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        if (differ.isDifferent(oldData, newData)) {
          ByteArrayDifference difference = new ByteArrayDifference(oldData, newData);

          for (ImageDiffWorker worker : workers) {
            worker.run(difference);
          }
          
          oldData = newData;
        }
      }
    }
  }
}
