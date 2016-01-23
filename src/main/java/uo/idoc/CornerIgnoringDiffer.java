package uo.idoc;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

@Deprecated
public class CornerIgnoringDiffer implements ScreenshotDiffer {

  private static final int CORNER_SIZE_PX = 100;

  public boolean isDifferent(byte[] oldData, byte[] newData) {
    try {
      BufferedImage img1 = ImageIO.read(new ByteArrayInputStream(oldData));
      BufferedImage img2 = ImageIO.read(new ByteArrayInputStream(newData));
      return isDifferentIgnoringCorners(img1, img2);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  private static boolean isDifferentIgnoringCorners(BufferedImage img1, BufferedImage img2) {
    int height = img1.getHeight(); 
    int width = img1.getWidth();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int rgb1 = img1.getRGB(j, i);
        int rgb2 = img2.getRGB(j, i);

        if (rgb1 != rgb2 && 
            (i < height - CORNER_SIZE_PX) &&
            (i > CORNER_SIZE_PX) &&
            (j < width - CORNER_SIZE_PX) &&
            (j > CORNER_SIZE_PX)) {
          return true;
        }
      }
    }
    return false;
  }

}