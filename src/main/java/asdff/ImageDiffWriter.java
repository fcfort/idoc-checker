package asdff;

import com.google.common.base.Function;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

public class ImageDiffWriter {

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

          BufferedImage diffImage = ImageDiffer.produceDiffImage(oldImage, newImage);

          ImageIO.write(diffImage, "png", diffFile);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        
        return input;
      }
    };
  }
}
