package ru.zagrebneva.sbercourse.mail;

import ru.zagrebneva.sbercourse.models.Card;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EmailNotificationLetterConstructor {

    public static String createNotificationLetterBody(Card card){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(card.getCardExpiryDate());
        calendar.add(Calendar.MONTH, 1);

        SimpleDateFormat formatter = new SimpleDateFormat("01.MM.yyyy");
        String expiredDate = formatter.format(calendar.getTime());

        return "Извещаем вас, что " + expiredDate +
                " истекает срок действия" +
                " карты " + card.getNumber() + ". Просим обратиться в" +
                " отделение банка или онлайн приложение банка для перевыпуска карты.";
    }
}
