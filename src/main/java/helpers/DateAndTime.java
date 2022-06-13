package helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateAndTime {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

    public String getCurrentTime() {
        now = LocalDateTime.now();
        return dtf.format(now);
    }
}
