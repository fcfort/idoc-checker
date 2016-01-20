package uo.idoc;

import java.util.Arrays;

public class ScreenshotDiff {
  private final byte[] oldData;
  private final byte[] newData;
  
  public ScreenshotDiff(byte[] oldData, byte[] newData) {
    this.oldData = oldData;
    this.newData = newData;
  }
  
  public byte[] getOldData() {
    return oldData;
  }
  
  public byte[] getNewData() {
    return newData;
  }
  
  boolean isDifferent() {
    return !Arrays.equals(oldData, newData);
  }
}

