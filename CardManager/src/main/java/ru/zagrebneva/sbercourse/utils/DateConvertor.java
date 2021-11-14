package ru.zagrebneva.sbercourse.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateConvertor {

    public static LocalDate toLocaleDate(Date date){
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
