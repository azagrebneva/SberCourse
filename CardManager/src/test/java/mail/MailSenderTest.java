package mail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.zagrebneva.sbercourse.config.EmailConfiguration;
import ru.zagrebneva.sbercourse.mail.EmailService;
import ru.zagrebneva.sbercourse.mail.EmailServiceImpl;
import ru.zagrebneva.sbercourse.models.Card;
import ru.zagrebneva.sbercourse.models.Customer;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;

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

    @Test
    public void sendCardExpirationNotification(){

        Card card = new Card();
        card.setNumber("123456789012345678");
        Date date = new GregorianCalendar(2021, 11 - 1, 1).getTime();
        card.setCardExpiryDate(date);

        Customer customer= new Customer();
        customer.setName("Анна");
        customer.setMiddleName("Дмитриевна");
        customer.setSurname("Загребнева");
        customer.setEmail("anna.zagrebneva@gmail.com");

        emailService.sendExpiredCardMessage(card, customer);
    }

}