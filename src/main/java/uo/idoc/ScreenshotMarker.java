package uo.idoc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

import uo.idoc.difference.HouseDifference;
import uo.idoc.model.House;

public class ScreenshotMarker {

  private static final String HALO_IMG_RESOURCE = "halo-arrow.png";
  
  private final BufferedImage halo;
  
  public ScreenshotMarker() {
    try {
      halo = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(HALO_IMG_RESOURCE));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static final int OLD_COLOR = 0x00FF00E1;
  private static final int NEW_COLOR = 0x00FFEA00;
  private static final int LATITUDE_OFFSET_PX = 12;
  private static final int LONGITUDE_OFFSET_PX = 7;
  
  // assumes 0,0 lat long is at top left of image
  public BufferedImage markScreenshot(BufferedImage im, HouseDifference diff) {
    System.out.println(String.format("Marking %d new houses",diff.getOnlyInNew().size()));
    System.out.println(String.format("Marking %d old houses",diff.getOnlyInOld().size()));
    markHouses(im, diff.getOnlyInNew(), NEW_COLOR, true);
    markHouses(im, diff.getOnlyInOld(), OLD_COLOR, false);
    
    writeLegend(im);
    return im;
  }

  private void writeLegend(BufferedImage im) {    
    Graphics2D graph = im.createGraphics();
    
    int fontSize = 80;
    
    graph.setFont(new Font ("Courier New", 1, fontSize));
    graph.setColor(new Color(OLD_COLOR));
    graph.drawString("Fallen houses", fontSize*2, fontSize*2);
    graph.setColor(new Color(NEW_COLOR));
    graph.drawString("New Houses", fontSize*2, fontSize*3);
  }

  private void markHouses(BufferedImage im, Iterable<House> houses, int color, boolean isLeft) {    
    for (House h : houses) {
      int x = h.getLatitude();
      int y = h.getLongitude();
      drawHalo(im, x + LATITUDE_OFFSET_PX, y + LONGITUDE_OFFSET_PX, color, isLeft);
    }
  }  
  
  private void drawHalo(BufferedImage im, int x, int y, int color, boolean isLeft) {
    Graphics2D graph = im.createGraphics();
    AffineTransform t = new AffineTransform();
    int offset = 25;
    int rotationDegrees = (isLeft ? 1 : -1) * 45;
    t.rotate(Math.toRadians(rotationDegrees), x, y);
    graph.setTransform(t);
    graph.drawImage(addHueToImage(halo, color), null, x - offset, y - offset);
    graph.dispose();
  }

  public static BufferedImage addHueToImage(BufferedImage im, final int color) {
    ImageFilter filter = new RGBImageFilter() {
      public final int filterRGB(int x, int y, int rgb) {
        return (rgb ^ color);
      }
    };

    ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
    return toBufferedImage(Toolkit.getDefaultToolkit().createImage(ip));
  }

  /**
   * Converts a given Image into a BufferedImage
   * @param img The Image to be converted
   * @return The converted BufferedImage
   */
  public static BufferedImage toBufferedImage(Image img) {
    if (img instanceof BufferedImage) {
      return (BufferedImage) img;
    }

    // Create a buffered image with transparency
    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    // Draw the image on to the buffered image
    Graphics2D bGr = bimage.createGraphics();
    bGr.drawImage(img, 0, 0, null);
    bGr.dispose();

    // Return the buffered image
    return bimage;
  }

}
