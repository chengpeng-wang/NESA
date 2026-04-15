package com.mb.bdapp.util;

import android.os.Environment;
import android.text.TextUtils;
import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtils {
    private static final String HASH_ALGORITHM = "MD5";
    private static final int RADIX = 36;
    private static final String TAG = FileUtils.class.getSimpleName();

    public static File getRootFile() {
        try {
            File rootFile = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/Android/data/du").toString());
            if (rootFile.exists()) {
                return rootFile;
            }
            rootFile.mkdirs();
            return rootFile;
        } catch (Exception e) {
            LogUtil.e(TAG, e.getLocalizedMessage());
            return null;
        }
    }

    public static File getFile(String fileName) {
        File parentFile = getRootFile();
        if (parentFile != null) {
            return new File(parentFile, fileName);
        }
        return null;
    }

    public static String getAPKPathByPname(String pname) {
        File to = getFile(new StringBuilder(String.valueOf(pname)).append(".apk").toString());
        if (to != null) {
            return to.getAbsolutePath();
        }
        return null;
    }

    public static String getAPKName(String pname) {
        return new StringBuilder(String.valueOf(pname)).append(".apk").toString();
    }

    public static void delete(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    public static String generateFileName(String imageUri) {
        String code = getStringMD5(imageUri);
        if (TextUtils.isEmpty(code)) {
            return getStringHashCode(imageUri);
        }
        return code;
    }

    private static String getStringHashCode(String value) {
        return String.valueOf(value.hashCode());
    }

    private static String getStringMD5(String value) {
        try {
            return new BigInteger(getMD5(value.getBytes())).abs().toString(36);
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] getMD5(byte[] data) {
        byte[] hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(data);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            LogUtil.e(TAG, e.getLocalizedMessage());
            return hash;
        }
    }
}
