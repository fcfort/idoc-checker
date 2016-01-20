package uo.idoc;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

public class ImageDiffWriter implements DiffWorker {

  private final String diffPath;

  public ImageDiffWriter(String diffPath) {
    this.diffPath = diffPath;
  }

  public void run(ScreenshotDiff diff) {
    try {
      File diffFile = new File(diffPath, UUID.randomUUID().toString() + ".png");
      System.out.println("Found difference! Creating diff image: " + diffFile.toPath().toString());
      BufferedImage oldImage = ImageIO.read(new ByteArrayInputStream(diff.getOldData()));
      BufferedImage newImage = ImageIO.read(new ByteArrayInputStream(diff.getNewData()));

      BufferedImage diffImage = ImageDiffer.produceDiffImage(oldImage, newImage);

      ImageIO.write(diffImage, "png", diffFile);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
