package uo.idoc;

public interface ScreenshotDiffer {
  boolean isDifferent(byte[] oldData, byte[] newData);
}
