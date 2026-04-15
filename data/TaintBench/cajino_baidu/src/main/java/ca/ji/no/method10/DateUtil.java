package ca.ji.no.method10;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static String dateUtil(long date) {
        Date d = new Date();
        d.setTime(date);
        return new SimpleDateFormat("yyyy��MM��dd�� HH:mm:ss", Locale.CHINA).format(d);
    }
}
