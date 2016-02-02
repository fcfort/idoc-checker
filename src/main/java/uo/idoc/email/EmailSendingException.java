package uo.idoc.email;

public class EmailSendingException extends RuntimeException {

  public EmailSendingException(Exception e) {
    super(e);
  }
}
