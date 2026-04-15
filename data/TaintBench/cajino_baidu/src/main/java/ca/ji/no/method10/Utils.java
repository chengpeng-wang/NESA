package ca.ji.no.method10;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
    public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
    public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
    public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
    protected static final String EXTRA_ACCESS_TOKEN = "access_token";
    public static final String EXTRA_MESSAGE = "message";
    public static final String RESPONSE_CONTENT = "content";
    public static final String RESPONSE_ERRCODE = "errcode";
    public static final String RESPONSE_METHOD = "method";
    public static final String TAG = "PushDemoActivity";
    public static String logStringCache = "";

    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (ai != null) {
                metaData = ai.metaData;
            }
            if (metaData != null) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {
        }
        return apiKey;
    }

    public static boolean hasBind(Context context) {
        if ("ok".equalsIgnoreCase(PreferenceManager.getDefaultSharedPreferences(context).getString("bind_flag", ""))) {
            return true;
        }
        return false;
    }

    public static void setBind(Context context, boolean flag) {
        String flagStr = "not";
        if (flag) {
            flagStr = "ok";
        }
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("bind_flag", flagStr);
        editor.commit();
    }

    public static List<String> getTagsList(String originalText) {
        if (originalText == null || originalText.equals("")) {
            return null;
        }
        List<String> tags = new ArrayList();
        int indexOfComma = originalText.indexOf(44);
        while (indexOfComma != -1) {
            tags.add(originalText.substring(0, indexOfComma));
            originalText = originalText.substring(indexOfComma + 1);
            indexOfComma = originalText.indexOf(44);
        }
        tags.add(originalText);
        return tags;
    }

    public static String getLogText(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("log_text", "");
    }

    public static void setLogText(Context context, String text) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("log_text", text);
        editor.commit();
    }
}
