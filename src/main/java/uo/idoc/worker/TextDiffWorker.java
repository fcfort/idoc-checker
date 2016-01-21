package uo.idoc.worker;

import uo.idoc.difference.StringDifference;

public interface TextDiffWorker {
  public void run(StringDifference diff);
}
