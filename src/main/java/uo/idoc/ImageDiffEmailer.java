package uo.idoc;

import java.util.List;

public class ImageDiffEmailer implements DiffWorker {
  
  private final String IDOC_SUBJECT = "Found IDOC!";
  
  private final GmailEmailer emailer;
  private final List<String> recipients;
 
  public ImageDiffEmailer(GmailEmailer emailer, List<String> recipients) {
    this.emailer = emailer;
    this.recipients = recipients;
  }
  
  public void run(ScreenshotDiff diff) {
    this.emailer.sendEmail(recipients, IDOC_SUBJECT, IDOC_SUBJECT);
  }

}
