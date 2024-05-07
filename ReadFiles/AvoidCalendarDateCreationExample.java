import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class AvoidCalendarDateCreationExample {
    private Date bad1() {
        return Calendar.getInstance().getTime(); // now
    }
    private Date good1a() {
        return new Date(); // now
    }
    private LocalDateTime good1b() {
        return LocalDateTime.now();
    }
    private long bad2() {
        return Calendar.getInstance().getTimeInMillis();
    }
    private long good2() {
        return System.currentTimeMillis();
    }
}