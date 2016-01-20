package uo.idoc;

import java.util.Arrays;

public class SimpleByteDiffer implements ScreenshotDiffer {

  public boolean isDifferent(byte[] oldData, byte[] newData) {
    return !Arrays.equals(oldData, newData);
  }
}
