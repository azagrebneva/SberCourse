package sber.course.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
public class CachingResult {

    private Date creationDate;

    private int number;

    private long result;

    @Override
    public String toString() {
        return "date = " + creationDate +
                ", " + number +
                "! = " + result;
    }
}
