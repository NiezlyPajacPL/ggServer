package helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateAndTime {

    public static String getCurrentTime() {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dtf.format(LocalDateTime.now());
    }
}
