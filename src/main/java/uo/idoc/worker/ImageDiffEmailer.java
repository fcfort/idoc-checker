package uo.idoc.worker;

import java.util.List;

import uo.idoc.difference.ByteArrayDifference;
import uo.idoc.email.GmailUnsafeService;

public class ImageDiffEmailer implements ImageDiffWorker {
  
  private final String IDOC_SUBJECT = "Found IDOC!";
  
  private final GmailUnsafeService emailer;
  private final List<String> recipients;
 
  public ImageDiffEmailer(GmailUnsafeService emailer, List<String> recipients) {
    this.emailer = emailer;
    this.recipients = recipients;
  }
  
  public void run(ByteArrayDifference diff) {
    this.emailer.sendEmail(recipients, IDOC_SUBJECT, IDOC_SUBJECT);
  }

}
