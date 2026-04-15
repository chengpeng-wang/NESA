package com.vr.installer.scanner;

import android.content.Intent;
import android.os.Build.VERSION;
import android.util.Log;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Support {
    public static final int MSEC_PER_MINUTE = 60000;
    public static final int SEC_PER_MINUTE = 60;

    public static String getMD5(String value) {
        String result = null;
        try {
            byte[] md5Digest = MessageDigest.getInstance("MD5").digest(value.getBytes());
            return String.format("%032x", new Object[]{new BigInteger(1, md5Digest)});
        } catch (NoSuchAlgorithmException e) {
            Log.e(Support.class.getName(), e.getMessage());
            return result;
        }
    }

    public static void correctIntent(Intent intent) {
        if (VERSION.SDK_INT >= 12) {
            intent.addFlags(32);
        }
    }
}
