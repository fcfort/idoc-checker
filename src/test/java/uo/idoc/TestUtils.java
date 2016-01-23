package uo.idoc;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

public final class TestUtils {

  public static String readResourceAsString(String resource) {
    try {
      return CharStreams.toString(
          new InputStreamReader(TestUtils.class.getClassLoader().getResourceAsStream(resource), Charsets.UTF_8));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static BufferedImage readRourceAsImage(String resource) {
    try {
      return ImageIO.read(TestUtils.class.getClassLoader().getResourceAsStream(resource));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
