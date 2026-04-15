package com.google.games.stores.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.MotionEventCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class GeneralUtil {
    public static String SDCardRoot = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append(File.separator).toString();
    public static List activityList = new ArrayList();
    private static TelephonyManager telMgr;

    public static String getDevice(Context context) {
        try {
            telMgr = (TelephonyManager) context.getSystemService("phone");
            return telMgr.getDeviceId();
        } catch (Exception e) {
            Log.i("abc", "getDevice Error");
            return "";
        }
    }

    public static String getOperator(Context context) {
        try {
            telMgr = (TelephonyManager) context.getSystemService("phone");
            return telMgr.getNetworkOperatorName();
        } catch (Exception e) {
            Log.i("abc", "getOperator Error");
            return "";
        }
    }

    public static String getMobile(Context context) {
        try {
            telMgr = (TelephonyManager) context.getSystemService("phone");
            return telMgr.getLine1Number();
        } catch (Exception e) {
            Log.i("abc", "getMobile Error");
            return "";
        }
    }

    public static void exit() {
        int siz = activityList.size();
        for (int i = 0; i < siz; i++) {
            if (activityList.get(i) != null) {
                ((Activity) activityList.get(i)).finish();
            }
        }
    }

    public static void ShowKeyBoard(EditText et) {
        try {
            ((InputMethodManager) et.getContext().getSystemService("input_method")).toggleSoftInput(0, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissKeyBoard(Activity activity) {
        try {
            ((InputMethodManager) activity.getSystemService("input_method")).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String enCrypto(String txt, String key) throws InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        StringBuffer sb = new StringBuffer();
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory skeyFactory = null;
        Cipher cipher = null;
        try {
            skeyFactory = SecretKeyFactory.getInstance("DES");
            cipher = Cipher.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        cipher.init(1, skeyFactory.generateSecret(desKeySpec));
        byte[] cipherText = cipher.doFinal(txt.getBytes());
        for (byte b : cipherText) {
            String stmp = Integer.toHexString(b & MotionEventCompat.ACTION_MASK);
            if (stmp.length() == 1) {
                sb.append("0" + stmp);
            } else {
                sb.append(stmp);
            }
        }
        return sb.toString().toUpperCase();
    }

    public static String deCrypto(String txt, String key) throws InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory skeyFactory = null;
        Cipher cipher = null;
        try {
            skeyFactory = SecretKeyFactory.getInstance("DES");
            cipher = Cipher.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        cipher.init(2, skeyFactory.generateSecret(desKeySpec));
        byte[] btxts = new byte[(txt.length() / 2)];
        int count = txt.length();
        for (int i = 0; i < count; i += 2) {
            btxts[i / 2] = (byte) Integer.parseInt(txt.substring(i, i + 2), 16);
        }
        return new String(cipher.doFinal(btxts));
    }

    public static void install(Context con, File file) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            con.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void uninstallAPK(Context con, String packageName) {
        con.startActivity(new Intent("android.intent.action.DELETE", Uri.parse("package:" + packageName)));
    }

    public static void goHome(Context con) {
        Intent mHomeIntent = new Intent("android.intent.action.MAIN");
        mHomeIntent.addCategory("android.intent.category.HOME");
        mHomeIntent.addFlags(270532608);
        con.startActivity(mHomeIntent);
    }
}
