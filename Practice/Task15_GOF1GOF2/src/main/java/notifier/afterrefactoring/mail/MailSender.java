package notifier.afterrefactoring.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MailSender {

    final static private JavaMailSender mailSender = createMailSender();

    private static JavaMailSender createMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties properties = mailSender.getJavaMailProperties();
        try {
            properties.load(new FileInputStream("mail.properties"));
            String password = properties.getProperty("mail.password");
            mailSender.setPassword(password);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("File \"mail.properties\" wasn't found.", e);
        }
        return mailSender;
    }

    public void send(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public static void sendHtml(String to, String subject, String textHtml){

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            // setting message text, last parameter 'true' says that it is HTML format
            helper.setText(textHtml, true);
            helper.setSubject(subject);
            // send the message
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Mistakes in HTML message sending.", e);
        }
    }
}
