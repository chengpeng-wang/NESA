package com.qc.util;

import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    public static Date nextWeek(Date currentDate) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(currentDate);
        cal.add(5, 7);
        return cal.getTime();
    }

    public static Date getSunday(Date monday) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(monday);
        cal.add(5, 6);
        return cal.getTime();
    }

    public static Date nextMonth(Date currentDate) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(currentDate);
        cal.add(2, 1);
        return cal.getTime();
    }

    public static Date nextYear(Date currentDate) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(currentDate);
        cal.add(1, 1);
        return cal.getTime();
    }
}
