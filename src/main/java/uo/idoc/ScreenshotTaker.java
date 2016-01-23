package uo.idoc;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicates;

// http://stackoverflow.com/questions/13832322/how-to-capture-the-screenshot-of-only-a-specific-element-using-selenium-webdrive
public class ScreenshotTaker {

  private final int waitTimeSeconds;
  private final String url;
  private final int imageHeight;
  private final int imageWidth;
  private final String tagName;

  public ScreenshotTaker(String url, int imageHeight, int imageWidth, int waitTimeSeconds, String tagName) throws IOException {
    this.imageHeight = imageHeight;
    this.imageWidth = imageWidth;
    this.waitTimeSeconds = waitTimeSeconds;
    this.url = url;
    this.tagName = tagName;
  }

  public BufferedImage takePartialScreenshot() throws IOException {
    String tempFileURI = createIframeHtml(imageHeight, imageWidth, url).toURI().toString();
    WebDriver driver = new Augmenter().augment(new FirefoxDriver());
    // Launch driver
    driver.get(tempFileURI);       

    // Wait for the page to load
    try {
      (new WebDriverWait(driver, waitTimeSeconds)).until(Predicates.<WebDriver> alwaysFalse());
    } catch (TimeoutException e) {
    }

    byte[] screenshotData = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

    //
    driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
    WebElement ele = driver.findElement(By.tagName(tagName));
    // WebElement ele = driver.findElement(By.id("OpenLayers.Layer.Vector_687_svgRoot"));
    
    //Get the location of element on the page
    Point point = ele.getLocation();;
    //Crop the entire page screenshot to get only element screenshot
    BufferedImage fullImg = ImageIO.read(new ByteArrayInputStream(screenshotData));
    BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), ele.getSize().getWidth(),
        ele.getSize().getHeight());
    
    driver.close();

    return eleScreenshot;
  }
  
  public BufferedImage takeScreenshot() throws IOException {
    String tempFileURI = createIframeHtml(imageHeight, imageWidth, url).toURI().toString();
    WebDriver driver = new Augmenter().augment(new FirefoxDriver());
    // Launch driver
    driver.get(tempFileURI);
    
    //driver.findElement(By.tagName("svg"));
   

    System.out.println("Waiting for page to load");
    // Wait for the page to load
    try {
      (new WebDriverWait(driver, waitTimeSeconds)).until(Predicates.<WebDriver> alwaysFalse());
    } catch (TimeoutException e) {
    }

    System.out.println("Taking screenshot");
    byte[] screenshotData = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

    driver.close();
    
    return ImageIO.read(new ByteArrayInputStream(screenshotData));
  }

  private static File createIframeHtml(int height, int width, String url) throws IOException {
    String data = String.format(
        "<html><body><iframe style=\"height: %dpx; width: %dpx;\" src=\"%s\"></iframe><body></html>", height, width,
        url);

    // Write iframe html
    File tempFile = File.createTempFile("iframe", ".html");
    Files.write(tempFile.toPath(), data.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    return tempFile;
  } 

}
