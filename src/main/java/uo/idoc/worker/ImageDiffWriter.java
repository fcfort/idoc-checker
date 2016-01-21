package uo.idoc.worker;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import uo.idoc.DiffImageMaker;
import uo.idoc.difference.ByteArrayDifference;

public class ImageDiffWriter implements ImageDiffWorker {

  private final String diffPath;

  public ImageDiffWriter(String diffPath) {
    this.diffPath = diffPath;
  }

  public void run(ByteArrayDifference diff) {
    try {
      File diffFile = new File(diffPath, new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".png");
      System.out.println("Found difference! Creating diff image: " + diffFile.toPath().toString());
      BufferedImage oldImage = ImageIO.read(new ByteArrayInputStream(diff.getOldData()));
      BufferedImage newImage = ImageIO.read(new ByteArrayInputStream(diff.getNewData()));

      BufferedImage diffImage = DiffImageMaker.produceDiffImage(oldImage, newImage);

      ImageIO.write(diffImage, "png", diffFile);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
