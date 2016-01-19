package asdff;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Date;

public class ScreenshotRunner implements Runnable {
  private final String tempFileURI;
  private byte[] oldData;
  private final int intervalSeconds;

  private static File createIframeHtml(int height, int width, String url) throws IOException {
    String data = String.format(
        "<html><body><iframe style=\"height: %dpx; width: %dpx;\" src=\"%s\"></iframe><body></html>",
        height, width, url);

    // Write iframe html
    File tempFile = File.createTempFile("iframe", ".html");
    Files.write(tempFile.toPath(), data.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    return tempFile;
  }

  private final Function<ScreenshotDiff, ScreenshotDiff> work;
  private final WebDriver driver;

  public ScreenshotRunner(String url, int imageHeight, int imageWidth, int intervalSeconds,
      Function<ScreenshotDiff, ScreenshotDiff> work) throws IOException {
    File tempFile = createIframeHtml(imageHeight, imageWidth, url);
    this.intervalSeconds = intervalSeconds;
    tempFileURI = tempFile.toURI().toString();
    this.work = work;
    WebDriver ffDriver = new FirefoxDriver();
    driver = new Augmenter().augment(ffDriver);
  }

  public void run() {
    while (true) {
      // Launch driver
      driver.get(tempFileURI);

      // Wait for the page to load
      try {
        (new WebDriverWait(driver, intervalSeconds))
            .until(new Predicate<WebDriver>() {
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
        ScreenshotDiff diff = new ScreenshotDiff(oldData, newData);
        if (diff.isDifferent()) {
          oldData = newData;
          work.apply(diff);          
        }
      }
    }
  }
}
