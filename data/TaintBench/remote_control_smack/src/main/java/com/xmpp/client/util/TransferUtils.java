package com.xmpp.client.util;

import android.support.v4.media.session.PlaybackStateCompat;

public class TransferUtils {
    public static String calculateSpeed(long bytediff, long timediff) {
        double kB = calculateSpeedLong(bytediff, timediff);
        if (bytediff == 0 && timediff == 0) {
            return "";
        }
        if (kB < 1024.0d) {
            return new StringBuilder(String.valueOf(splitAtDot(Double.toString(kB), 1))).append("kB/s").toString();
        }
        return new StringBuilder(String.valueOf(splitAtDot(Double.toString(kB / 1024.0d), 1))).append("MB/s").toString();
    }

    public static double calculateSpeedLong(long bytediff, long timediff) {
        if (timediff == 0) {
            timediff = 1;
        }
        return (((double) (bytediff / timediff)) * 1000.0d) / 1024.0d;
    }

    public static String calculateEstimate(long currentsize, long totalsize, long timestart, long timenow) {
        long timediff = timenow - timestart;
        long sizeleft = totalsize - currentsize;
        if (currentsize == 0) {
            currentsize = 1;
        }
        return convertSecondstoHHMMSS(Math.round((float) (((sizeleft * timediff) / currentsize) / 1000)));
    }

    public static String convertSecondstoHHMMSS(int second) {
        int hours = Math.round((float) (second / 3600));
        int minutes = Math.round((float) ((second / 60) % 60));
        int seconds = Math.round((float) (second % 60));
        String hh = hours;
        String mm = minutes;
        return "(" + hh + ":" + mm + ":" + (seconds) + ")";
    }

    public static String getAppropriateByteWithSuffix(long bytes) {
        if (bytes >= 1099511627776L) {
            return new StringBuilder(String.valueOf(splitAtDot((bytes / 1099511627776L), 2))).append(" TB").toString();
        }
        if (bytes >= 1073741824) {
            return new StringBuilder(String.valueOf(splitAtDot((bytes / 1073741824), 2))).append(" GB").toString();
        }
        if (bytes >= 1048576) {
            return new StringBuilder(String.valueOf(splitAtDot((bytes / 1048576), 2))).append(" MB").toString();
        }
        if (bytes >= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
            return new StringBuilder(String.valueOf(splitAtDot((bytes / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID), 2))).append(" KB").toString();
        }
        return new StringBuilder(String.valueOf(bytes)).append(" B").toString();
    }

    private static String splitAtDot(String string, int significantdigits) {
        if (!string.contains(".")) {
            return string;
        }
        String s = string.replace(".", "T").split("T")[1];
        if (s.length() >= significantdigits) {
            return string.substring(0, (string.indexOf(".") + 1) + significantdigits);
        }
        return string.substring(0, (string.indexOf(".") + 1) + s.length());
    }
}
