package viktoriia.vihriian.taskmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateFormatter {

    public static final String DATE_FORMAT_FOR_HUMANS = "EEEE, d MMMM, yyyy ' ' HH:mm";
    private static final SimpleDateFormat dateFormatForHumans = new
            SimpleDateFormat(DATE_FORMAT_FOR_HUMANS);

    public static final String DATE_FORMAT = "yyyyMMddHHmmss";
    private static final SimpleDateFormat dateFormat = new
            SimpleDateFormat(DATE_FORMAT);/*

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
    }*/

    public static String getStringFromFormattedLong(long l) {
            Calendar c = new GregorianCalendar();
            c.setTimeInMillis(l * 1000);
            dateFormatForHumans.setCalendar(c);
            return dateFormatForHumans.format(c.getTime());
    }

    public static long getTimeFromLong(long l) {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(l * 1000);
        return c.getTimeInMillis();
    }
    public static boolean isActual(long time) {
        if(time * 1000 > System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

}
