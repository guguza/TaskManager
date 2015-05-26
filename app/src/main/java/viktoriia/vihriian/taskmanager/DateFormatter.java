package viktoriia.vihriian.taskmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ������������� on 26.05.2015.
 */
public class DateFormatter {

    public static final String DATE_FORMAT_FOR_HUMANS = "EEEE, d MMMM, yyyy ' ' h:mm";
    private static final SimpleDateFormat dateFormatForHumans = new
            SimpleDateFormat(DATE_FORMAT_FOR_HUMANS);

    public static final String DATE_FORMAT = "yyyyMMddHHmmss";
    private static final SimpleDateFormat dateFormat = new
            SimpleDateFormat(DATE_FORMAT);

    public static long formatDateAsLong(Calendar cal){
        return Long.parseLong(dateFormat.format(cal.getTime()));
    }

    public static Calendar getCalendarFromFormattedLong(long l){
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(dateFormat.parse(String.valueOf(l)));
            return c;

        } catch (ParseException e) {
            return null;
        }
    }

    public static String getStringFromFormattedLong(long l){
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(dateFormat.parse(String.valueOf(l)));
            return dateFormatForHumans.format(c.getTime());
        } catch (ParseException e) {
            return null;
        }
    }
}