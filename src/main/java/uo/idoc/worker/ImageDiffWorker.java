package uo.idoc.worker;

import uo.idoc.difference.ByteArrayDifference;

public interface ImageDiffWorker {
  public void run(ByteArrayDifference diff);
}
