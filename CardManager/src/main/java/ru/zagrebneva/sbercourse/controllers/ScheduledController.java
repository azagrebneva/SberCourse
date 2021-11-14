package ru.zagrebneva.sbercourse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.zagrebneva.sbercourse.config.EmailConfiguration;
import ru.zagrebneva.sbercourse.dao.CustomerDAO;
import ru.zagrebneva.sbercourse.mail.EmailNotificationLetterConstructor;
import ru.zagrebneva.sbercourse.mail.EmailService;
import ru.zagrebneva.sbercourse.mail.EmailServiceImpl;
import ru.zagrebneva.sbercourse.models.Card;
import ru.zagrebneva.sbercourse.models.Customer;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Controller
@RequestMapping("/")
public class ScheduledController {

    private final EmailService emailService;

    private final CustomerDAO customerDAO;

    @Autowired
    public ScheduledController(CustomerDAO customerDAO){
        this.customerDAO = customerDAO;
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(EmailConfiguration.class);
        emailService = context.getBean("emailService", EmailServiceImpl.class);
    }

    //@Scheduled(fixedRate= 10_000)
    @Async
    @Scheduled(cron = "${interval-in-cron}")
    public void ScheduledFixedRate(){
        List<List<?>> lists = customerDAO.getExpiredСards(new Date());

        for (List<?> list: lists) {
            Card card = (Card) list.get(0);
            Customer customer = (Customer) list.get(1);
            emailService.sendExpiredCardMessage(card, customer);
        }
    }
}
