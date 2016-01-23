package uo.idoc.email;

import java.util.List;

public class Emailer {

  private final GmailEmailer gmailer;
  private final List<String> recipients;

public Emailer(GmailEmailer gmailer, List<String> recipients) {
  this.gmailer = gmailer;
  this.recipients = recipients;
}

  public void sendEmail(String subject, String body) {
    gmailer.sendEmail(recipients, subject, body);
  }
}
