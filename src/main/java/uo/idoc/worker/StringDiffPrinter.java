package uo.idoc.worker;

import java.util.Arrays;
import java.util.List;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import uo.idoc.difference.StringDifference;

// https://code.google.com/p/java-diff-utils/wiki/SampleUsage
// http://stackoverflow.com/questions/13464954/how-do-i-split-a-string-by-line-break
public class StringDiffPrinter implements TextDiffWorker {

  public void run(StringDifference diff) {
    // Compute diff. Get the Patch object. Patch is the container for computed
    // deltas.
    Patch patch = DiffUtils.diff(stringToList(diff.getOldData()), stringToList(diff.getNewData()));

    System.out.println("Making patch deltas");
    for (Delta delta : patch.getDeltas()) {
      System.out.println(delta);
    }
  }

  private static List<String> stringToList(String s) {
    return Arrays.asList(s.split("\\r?\\n"));
  }

  public static void main(String... args) {
    StringDifference diff = new StringDifference("asdf asdf\\nasdfa\nasdfn", "asdfasdf\nasdfasdf\nadf");
    new StringDiffPrinter().run(diff);
  }
}
