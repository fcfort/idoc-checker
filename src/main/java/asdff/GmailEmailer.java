package asdff;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

// http://stackoverflow.com/questions/46663/how-can-i-send-an-email-by-java-application-using-gmail-yahoo-or-hotmail
public class GmailEmailer {
  private static final String HOST = "smtp.gmail.com";
  private static final String PORT = "587";
  
  private final String username; // GMail user name (just the part before "@gmail.com")
  private final String password; // GMail password

  public GmailEmailer(String username, String password) {
    this.username = username;
    this.password = password;
  }
  
  public void sendEmail(List<String> recipients, String subject, String body) {
    sendFromGMail(recipients, subject, body);
  }

  private void sendFromGMail(List<String> to, String subject, String body) {
    Properties props = System.getProperties();
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", HOST);
    props.put("mail.smtp.user", username);
    props.put("mail.smtp.password", password);
    props.put("mail.smtp.port", PORT);
    props.put("mail.smtp.auth", "true");

    Session session = Session.getDefaultInstance(props);
    MimeMessage message = new MimeMessage(session);

    try {
      message.setFrom(new InternetAddress(username));

      // To get the array of addresses
      for(String t : to) {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(t));
      }

      message.setSubject(subject);
      message.setText(body);
      Transport transport = session.getTransport("smtp");
      transport.connect(HOST, username, password);
      transport.sendMessage(message, message.getAllRecipients());
      transport.close();
    } catch (AddressException ae) {
      ae.printStackTrace();
    } catch (MessagingException me) {
      me.printStackTrace();
    }
  }
}
