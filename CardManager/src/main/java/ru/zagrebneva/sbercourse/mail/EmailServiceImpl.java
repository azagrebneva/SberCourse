package ru.zagrebneva.sbercourse.mail;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import ru.zagrebneva.sbercourse.models.Card;
import ru.zagrebneva.sbercourse.models.Customer;


@Service("emailService")
public class EmailServiceImpl implements EmailService {

    private static final String NOREPLY_ADDRESS = "anna.zagrebneva@gmail.com";

    private final JavaMailSender emailSender;

    private final SimpleMailMessage template;

    private final SpringTemplateEngine thymeleafTemplateEngine;

    @Value("classpath:/mail-templates/trust_love.jpg")
    private Resource resourceFile;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender,
                            SimpleMailMessage template, SpringTemplateEngine thymeleafTemplateEngine) {
        this.emailSender = emailSender;
        this.template = template;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
    }

    @Override
    public void sendSimpleMessage(String to,
                                  String subject,
                                  String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NOREPLY_ADDRESS);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void sendSimpleMessageUsingTemplate(String to,
                                               String subject,
                                               String ...templateModel) {
        String text = String.format(Objects.requireNonNull(template.getText()), (Object) templateModel);
        System.out.println(text);
        sendSimpleMessage(to, subject, text);
    }

    @Override
    public void sendMessageUsingThymeleafTemplate(String to,
                                                  String toName,
                                                  String subject,
                                                  String text)
            throws MessagingException {

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipientName", toName);
        templateModel.put("text", text);

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);

        String htmlBody = thymeleafTemplateEngine.process(
                "template-thymeleaf.html", thymeleafContext);

        System.out.println(htmlBody);
        sendHtmlMessage(to, subject, htmlBody);
    }


    private void sendHtmlMessage(String to,
                                 String subject,
                                 String htmlBody) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(NOREPLY_ADDRESS);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.addInline("attachment.png", resourceFile);
        emailSender.send(message);
    }

    @Override
    public void sendExpiredCardMessage(Card card,
                                       Customer customer){
        if ((card == null) || (customer == null) ||
                (customer.getEmail() == null)) {
            return;
        }

        try {
            sendMessageUsingThymeleafTemplate(
                    customer.getEmail(), customer.toString(),
                    "завершение срока действия банковской карты",
                    EmailNotificationLetterConstructor.createNotificationLetterBody(card));

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
   
}
