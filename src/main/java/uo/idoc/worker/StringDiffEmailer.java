package uo.idoc.worker;

import java.util.Arrays;
import java.util.List;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import uo.idoc.difference.StringDifference;
import uo.idoc.email.GmailUnsafeService;

// https://code.google.com/p/java-diff-utils/wiki/SampleUsage
// http://stackoverflow.com/questions/13464954/how-do-i-split-a-string-by-line-break
public class StringDiffEmailer implements TextDiffWorker {

  private final String IDOC_SUBJECT = "Found IDOC!";

  private final GmailUnsafeService emailer;
  private final List<String> recipients;

  public StringDiffEmailer(GmailUnsafeService emailer, List<String> recipients) {
    this.emailer = emailer;
    this.recipients = recipients;
  }

  public void run(StringDifference diff) {
    Patch patch = DiffUtils.diff(stringToList(diff.getOldData()), stringToList(diff.getNewData()));

    StringBuilder sb = new StringBuilder();
    for (Delta delta : patch.getDeltas()) {
      sb.append(delta.toString());
    }

    emailer.sendEmail(recipients, IDOC_SUBJECT, sb.toString());
  }

  private static List<String> stringToList(String s) {
    return Arrays.asList(s.split("\\r?\\n"));
  }
}
