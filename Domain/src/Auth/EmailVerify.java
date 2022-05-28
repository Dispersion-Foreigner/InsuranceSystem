package Auth;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailVerify {

  private int count;


  public void verifyEmail(ArrayList<String> emails) {

    count = 0;

    Properties p = System.getProperties();
    p.put("mail.smtp.starttls.enable", "true");
    p.put("mail.smtp.host", "smtp.naver.com");
    p.put("mail.smtp.auth", "true");
    p.put("mail.smtp.port", "587");

    Authenticator auth = new MyAuthentication();
    Session session = Session.getDefaultInstance(p, auth);
    MimeMessage message = new MimeMessage(session);

    // Compose the message
    try {
      message.setSentDate(new Date());
      InternetAddress from = new InternetAddress();

      from = new InternetAddress("Foreigners<hakgooyeol@naver.com>");
      message.setFrom(from);

      InternetAddress[] addresses = new InternetAddress[emails.size()];

      emails.forEach(email -> {
        try {
          addresses[count] = new InternetAddress(email);
          count = count + 1;
        } catch (AddressException e) {
          e.printStackTrace();
        }
      });

      message.addRecipients(Message.RecipientType.TO, addresses);

      // Subject
      message.setSubject("[전과자들] 보험료 체납 안내", "UTF-8");

      // Text
      message.setText("안녕하세요 전과자들입니다. 고객님의 보험료가 정상적으로 입금되지 않았습니다. 돈 내놔", "UTF-8");

      // send the message
      Transport.send(message);
      System.out.println(emails.size() + "명에게 성공적으로 청구서를 전송하였습니다.");

    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  class MyAuthentication extends Authenticator {

    PasswordAuthentication account;

    public MyAuthentication() {
      String id = "hakgooyeol";
      String pw = "5SS8XH6C5PVP";
      account = new PasswordAuthentication(id, pw);
    }

    public PasswordAuthentication getPasswordAuthentication() {
      return account;
    }
  }

}
