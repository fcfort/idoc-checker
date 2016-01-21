package uo.idoc.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import uo.idoc.DiffImageMaker;

/** Diffs two image files provided by the user */
public class ImageDiffMain {
  @Parameter(names = "--before", description = "File to use as the before image")
  private String beforeImageFile;

  @Parameter(names = "--after", description = "File to use as the after image")
  private String afterImageFile;

  @Parameter(names = "--outputPath", description = "Where to put the diff")
  private String outputPath;

  public static void main(String... args) throws IOException {
    System.out.println(Arrays.toString(args));
    ImageDiffMain main = new ImageDiffMain();
    new JCommander(main, args);
    main.run();
  }

  private void run() throws IOException {
    BufferedImage diff = DiffImageMaker.produceDiffImage(ImageIO.read(new File(beforeImageFile)),
        ImageIO.read(new File(afterImageFile)));
    ImageIO.write(diff, "png", new File(outputPath, "diff.png"));
  }
}
