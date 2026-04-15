package com.splunk.mint;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

class UidManager {
    private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final int UID_CORRECT_LENTH = 32;
    private static final String UID_ERROR_ID = "12345678901234567890123456789012";
    private static final String UID_FIELD = "UID_FIELD";
    private static final String UID_NOSAVED = "UID_NOSAVED";
    private static final String UID_PREFERENCES = "UIDPREFERENCES";

    UidManager() {
    }

    protected static final String getUid(Context ctx) {
        if (Properties.UID != null && Properties.UID.length() == 32) {
            return Properties.UID;
        }
        String uid = UID_ERROR_ID;
        if (ctx != null) {
            SharedPreferences sharedPrefs = ctx.getSharedPreferences(UID_PREFERENCES, 0);
            if (sharedPrefs != null) {
                uid = sharedPrefs.getString(UID_FIELD, UID_NOSAVED);
            }
        }
        if (!uid.equals(UID_NOSAVED) && !uid.equals(UID_ERROR_ID)) {
            return uid;
        }
        uid = generateUid();
        saveUid(ctx, uid);
        return uid;
    }

    protected static boolean saveUid(Context ctx, String uid) {
        if (uid == null || uid.length() != 32) {
            return false;
        }
        Properties.UID = uid;
        SharedPreferences sharedPrefs = ctx.getSharedPreferences(UID_PREFERENCES, 0);
        if (sharedPrefs != null) {
            return sharedPrefs.edit().putString(UID_FIELD, uid).commit();
        }
        return false;
    }

    private static final String generateUid() {
        String s1 = Long.valueOf(new Date().getTime()).toString();
        String s2 = new Object().toString();
        String s3 = String.valueOf(System.nanoTime());
        String s5 = "android_id";
        byte[] thedigest = null;
        try {
            thedigest = MessageDigest.getInstance("MD5").digest((s1 + s2 + s3 + Integer.toString((int) (new Random(System.currentTimeMillis()).nextDouble() * 65535.0d)) + "android_id").getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
        }
        return new String(encodeHex(thedigest));
    }

    private static final char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[(l << 1)];
        int j = 0;
        for (int i = 0; i < l; i++) {
            int i2 = j + 1;
            out[j] = DIGITS[(data[i] & 240) >>> 4];
            j = i2 + 1;
            out[i2] = DIGITS[data[i] & 15];
        }
        return out;
    }
}
