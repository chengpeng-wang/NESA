package cn.smsmanager.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeFormat {
    public static String getCurrentTimeString() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
