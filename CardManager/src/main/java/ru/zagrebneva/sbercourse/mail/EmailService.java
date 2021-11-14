package ru.zagrebneva.sbercourse.mail;

import ru.zagrebneva.sbercourse.models.Card;
import ru.zagrebneva.sbercourse.models.Customer;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailService {

    void sendSimpleMessage(String to,
                           String subject,
                           String text);

    void sendSimpleMessageUsingTemplate(String to,
                                        String subject,
                                        String... templateModel);

    void sendMessageUsingThymeleafTemplate(String to,
                                           String toName,
                                           String subject,
                                           String text) throws IOException, MessagingException;

    void sendExpiredCardMessage(Card card,
                                Customer customer);

}
