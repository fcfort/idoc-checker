package asdff;

import com.google.common.base.Function;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

public class ImageDiffWriter {
  private static final int DIFF_COLOR = 0x00FF3E96;

  private final String diffPath;

  public ImageDiffWriter(String diffPath) {
    this.diffPath = diffPath;
  }

  public Function<ScreenshotDiff, ScreenshotDiff> doWork() {
    return new Function<ScreenshotDiff, ScreenshotDiff>() {
      public ScreenshotDiff apply(ScreenshotDiff input) {
        try {
          File diffFile = new File(diffPath, UUID.randomUUID().toString() + ".png");
          System.out.println(
              "Found difference! Creating diff image: " + diffFile.toPath().toString());
          BufferedImage oldImage = ImageIO.read(new ByteArrayInputStream(input.getOldData()));
          BufferedImage newImage = ImageIO.read(new ByteArrayInputStream(input.getNewData()));

          BufferedImage diffImage = produceDiffImage(oldImage, newImage);

          ImageIO.write(diffImage, "png", diffFile);
        } catch (IOException e) {
          e.printStackTrace();
        }

        return null;
      }
    };
  }

  private static BufferedImage produceDiffImage(BufferedImage oldImage, BufferedImage newImage) {
    BufferedImage img1 = oldImage;
    BufferedImage img2 = newImage;

    int width1 = img1.getWidth(null);
    int width2 = img2.getWidth(null);
    int height1 = img1.getHeight(null);
    int height2 = img2.getHeight(null);

    assert ((width1 != width2) || (height1 != height2));

    if ((width1 != width2) || (height1 != height2)) {
      System.err.println("Error: Images dimensions mismatch");
      System.exit(1);
    }

    BufferedImage diffImage = new BufferedImage(width1, height1, BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < height1; i++) {
      for (int j = 0; j < width1; j++) {
        int rgb1 = img1.getRGB(j, i);
        int rgb2 = img2.getRGB(j, i);

        if (rgb1 == rgb2) {
          diffImage.setRGB(j, i, rgb1);
        } else {
          diffImage.setRGB(j, i, DIFF_COLOR);
        }
      }
    }

    return diffImage;
  }
}
