package mail;

import config.EmailConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.mail.MessagingException;
import java.io.IOException;

class MailSenderTest {

    private static EmailService emailService;

    @BeforeEach
    public void createEmailService(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(EmailConfiguration.class);
        emailService = context.getBean("emailService", EmailServiceImpl.class);
    }

    @Test
    public void sendSimpleMessage(){
        emailService.sendSimpleMessage("anna.zagrebneva@gmail.com",
                "hello",
                "Hello world!!!");
    }
    @Test
    public void sendSimpleMessageUsingTemplate(){
        emailService.sendSimpleMessageUsingTemplate("anna.zagrebneva@gmail.com",
                "hello",
                "Hello world!!!");
    }

    @Test
    public void sendMessageUsingThymeleafTemplate(){

        try {
            emailService.sendMessageUsingThymeleafTemplate(
                "anna.zagrebneva@gmail.com", "Анна Загребнева",
                "уведомление",
                "Желаем вам хорошего настроения!");

        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }
}