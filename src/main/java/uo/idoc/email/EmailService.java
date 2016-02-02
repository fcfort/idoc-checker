package uo.idoc.email;

import java.util.List;

public interface EmailService {
  public void sendEmail(
      List<String> recipients, String subject, String body) throws EmailSendingException;
}
