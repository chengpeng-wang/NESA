package com.baidu.android.pushservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.util.Log;
import com.baidu.android.pushservice.util.Internal;
import com.baidu.android.pushservice.util.PushDatabase;
import com.baidu.android.pushservice.util.k;
import com.baidu.android.pushservice.util.m;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class PushManager {
    private static int INFO_MAX_NUM = 50;
    private static final String TAG = "PushManager";
    private static HashMap mStatisticsMap = new HashMap();

    public static void activityStarted(Activity activity) {
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        Intent intent = activity.getIntent();
        String str = "" + intent.getIntExtra(PushConstants.EXTRA_OPENTYPE, 0);
        String stringExtra = intent.getStringExtra(PushConstants.EXTRA_MSGID);
        int hashCode = activity.hashCode();
        if (b.a()) {
            Log.d(TAG, "Collect Activity start feedback info , package:" + activity.getPackageName() + " timeStamp:" + currentTimeMillis + " openType: " + str + " msgid: " + stringExtra + " hashCode: " + hashCode);
        }
        insertAppStartInfo(activity.getPackageName(), hashCode, str, stringExtra, currentTimeMillis + "");
    }

    public static void activityStoped(Activity activity) {
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        int hashCode = activity.hashCode();
        if (b.a()) {
            Log.d(TAG, "Collect Activity stop feedback info , package:" + activity.getPackageName() + " timeStamp:" + currentTimeMillis + " hashCode: " + hashCode);
        }
        insertAppStopInfo(activity, activity.getPackageName(), hashCode, "" + currentTimeMillis);
    }

    public static void bind(Context context, int i) {
        if (!isNullContext(context)) {
            Intent createMethodIntent = createMethodIntent(context);
            createMethodIntent.putExtra("method", PushConstants.METHOD_BIND);
            createMethodIntent.putExtra(PushConstants.EXTRA_BIND_NAME, Build.MODEL);
            createMethodIntent.putExtra(PushConstants.EXTRA_BIND_STATUS, i);
            createMethodIntent.putExtra(PushConstants.EXTRA_PUSH_SDK_VERSION, 13);
            createMethodIntent.setFlags(createMethodIntent.getFlags() | 32);
            context.sendBroadcast(createMethodIntent);
        }
    }

    public static void bindGroup(Context context, String str) {
        if (!isNullContext(context)) {
            Intent createMethodIntent = createMethodIntent(context);
            createMethodIntent.putExtra("method", PushConstants.METHOD_GBIND);
            createMethodIntent.putExtra(PushConstants.EXTRA_GID, str);
            createMethodIntent.setFlags(createMethodIntent.getFlags() | 32);
            context.sendBroadcast(createMethodIntent);
        }
    }

    private static Intent createMethodIntent(Context context) {
        if (isNullContext(context)) {
            return null;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), 1);
        int i = sharedPreferences.getInt("com.baidu.android.pushservice.PushManager.LOGIN_TYPE", 0);
        String string = sharedPreferences.getString("com.baidu.android.pushservice.PushManager.LONGIN_VALUE", "");
        Intent createBdussInent;
        String rsaEncrypt;
        if (i == 2) {
            createBdussInent = Internal.createBdussInent(context);
            createBdussInent.putExtra("appid", string);
            rsaEncrypt = PushConstants.rsaEncrypt(sharedPreferences.getString("com.baidu.android.pushservice.PushManager.BDUSS", ""));
            createBdussInent.putExtra("bduss", rsaEncrypt);
            if (!b.a()) {
                return createBdussInent;
            }
            Log.d(TAG, "RSA Bduss:" + rsaEncrypt);
            return createBdussInent;
        }
        createBdussInent = PushConstants.createMethodIntent(context);
        if (i == 1) {
            rsaEncrypt = PushConstants.rsaEncrypt(string);
            createBdussInent.putExtra(PushConstants.EXTRA_ACCESS_TOKEN, rsaEncrypt);
            if (!b.a()) {
                return createBdussInent;
            }
            Log.d(TAG, "RSA Access Token:" + rsaEncrypt);
            return createBdussInent;
        }
        createBdussInent.putExtra(PushConstants.EXTRA_API_KEY, string);
        if (!b.a()) {
            return createBdussInent;
        }
        Log.d(TAG, "Api Key:" + string.substring(0, 5));
        return createBdussInent;
    }

    public static void delTags(Context context, List list) {
        if (!isNullContext(context)) {
            if (list == null || list.size() == 0) {
                Log.w(TAG, "No tags specified, do nothing.");
                return;
            }
            String str = "[";
            Iterator it = list.iterator();
            while (true) {
                String str2 = str;
                if (it.hasNext()) {
                    str = ((str2 + "\"") + ((String) it.next())) + "\",";
                } else {
                    str = str2.substring(0, str2.length() - 1) + "]";
                    Intent createMethodIntent = createMethodIntent(context);
                    createMethodIntent.putExtra("method", PushConstants.METHOD_DEL_TAGS);
                    createMethodIntent.putExtra(PushConstants.EXTRA_TAGS, str);
                    context.sendBroadcast(createMethodIntent);
                    return;
                }
            }
        }
    }

    public static void deleteMessages(Context context, String[] strArr) {
        if (!isNullContext(context) && strArr != null) {
            Intent createMethodIntent = createMethodIntent(context);
            createMethodIntent.putExtra("method", PushConstants.METHOD_DELETE);
            createMethodIntent.putExtra(PushConstants.EXTRA_MSG_IDS, strArr);
            context.sendBroadcast(createMethodIntent);
        }
    }

    public static void disableLbs(Context context) {
        if (!isNullContext(context)) {
            PushSettings.a(context, false);
        }
    }

    public static void enableLbs(Context context) {
        if (!isNullContext(context)) {
            PushSettings.a(context, true);
        }
    }

    public static void fetchGroupMessages(Context context, String str, int i, int i2) {
        if (!isNullContext(context)) {
            Intent createMethodIntent = createMethodIntent(context);
            createMethodIntent.putExtra("method", PushConstants.METHOD_FETCHGMSG);
            createMethodIntent.putExtra(PushConstants.EXTRA_GID, str);
            createMethodIntent.putExtra(PushConstants.EXTRA_GROUP_FETCH_TYPE, i);
            createMethodIntent.putExtra(PushConstants.EXTRA_GROUP_FETCH_NUM, i2);
            context.sendBroadcast(createMethodIntent);
        }
    }

    public static void fetchMessages(Context context, int i, int i2) {
        if (!isNullContext(context)) {
            Intent createMethodIntent = createMethodIntent(context);
            createMethodIntent.putExtra("method", PushConstants.METHOD_FETCH);
            createMethodIntent.putExtra(PushConstants.EXTRA_FETCH_TYPE, i);
            createMethodIntent.putExtra(PushConstants.EXTRA_FETCH_NUM, i2);
            context.sendBroadcast(createMethodIntent);
        }
    }

    public static void getGroupInfo(Context context, String str) {
        if (!isNullContext(context)) {
            Intent createMethodIntent = createMethodIntent(context);
            createMethodIntent.putExtra("method", PushConstants.METHOD_GINFO);
            createMethodIntent.putExtra(PushConstants.EXTRA_GID, str);
            context.sendBroadcast(createMethodIntent);
        }
    }

    public static void getGroupList(Context context) {
        if (!isNullContext(context)) {
            Intent createMethodIntent = createMethodIntent(context);
            createMethodIntent.putExtra("method", PushConstants.METHOD_GLIST);
            context.sendBroadcast(createMethodIntent);
        }
    }

    public static void getGroupMessageCounts(Context context, String str) {
        if (!isNullContext(context)) {
            Intent createMethodIntent = createMethodIntent(context);
            createMethodIntent.putExtra("method", PushConstants.METHOD_COUNTGMSG);
            createMethodIntent.putExtra(PushConstants.EXTRA_GID, str);
            context.sendBroadcast(createMethodIntent);
        }
    }

    public static void getMessageCounts(Context context) {
        if (!isNullContext(context)) {
            Intent createMethodIntent = createMethodIntent(context);
            createMethodIntent.putExtra("method", PushConstants.METHOD_COUNT);
            context.sendBroadcast(createMethodIntent);
        }
    }

    public static void init(Context context, String str) {
        if (!isNullContext(context)) {
            Editor edit = context.getSharedPreferences(context.getPackageName(), 0).edit();
            edit.putInt("com.baidu.android.pushservice.PushManager.LOGIN_TYPE", 1);
            edit.putString("com.baidu.android.pushservice.PushManager.LONGIN_VALUE", str);
            edit.commit();
            PushSettings.a(context.getApplicationContext());
            m.j(context);
        }
    }

    public static void init(Context context, String str, String str2) {
        if (!isNullContext(context)) {
            Editor edit = context.getSharedPreferences(context.getPackageName(), 0).edit();
            edit.putInt("com.baidu.android.pushservice.PushManager.LOGIN_TYPE", 2);
            edit.putString("com.baidu.android.pushservice.PushManager.LONGIN_VALUE", str);
            edit.putString("com.baidu.android.pushservice.PushManager.BDUSS", str2);
            edit.commit();
            PushSettings.a(context.getApplicationContext());
            m.j(context);
        }
    }

    public static void initFromAKSK(Context context, String str) {
        if (!isNullContext(context)) {
            Editor edit = context.getSharedPreferences(context.getPackageName(), 0).edit();
            edit.putInt("com.baidu.android.pushservice.PushManager.LOGIN_TYPE", 0);
            edit.putString("com.baidu.android.pushservice.PushManager.LONGIN_VALUE", str);
            edit.commit();
            PushSettings.a(context);
            m.j(context);
        }
    }

    private static void insertAppStartInfo(String str, int i, String str2, String str3, String str4) {
        if (mStatisticsMap.size() < INFO_MAX_NUM) {
            k kVar = new k();
            kVar.a = i;
            kVar.b = str;
            kVar.c = str2;
            kVar.d = str3;
            kVar.e = str4;
            mStatisticsMap.put(Integer.valueOf(kVar.a), kVar);
        }
    }

    private static void insertAppStopInfo(Context context, String str, int i, String str2) {
        if (!isNullContext(context) && mStatisticsMap != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName() + "pst", 1);
            String string = sharedPreferences.getString("cache_when_exception", "");
            if (!string.equals("")) {
                k kVar = new k();
                String[] split = string.split("#");
                if (split.length == 5 || split.length == 6) {
                    kVar.b = split[0];
                    kVar.c = split[1];
                    kVar.d = split[2];
                    kVar.e = split[3];
                    kVar.g = split[4];
                    if (split.length == 6) {
                        try {
                            kVar.h = new JSONObject(split[5]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    PushDatabase.insertStatisticsInfo(PushDatabase.getDb(context), kVar);
                    Editor edit = sharedPreferences.edit();
                    edit.putString("cache_when_exception", "");
                    edit.commit();
                }
            }
            k kVar2 = (k) mStatisticsMap.remove(Integer.valueOf(i));
            if (kVar2 != null) {
                kVar2.f = str2;
                kVar2.g = "" + (Long.parseLong(kVar2.f) - Long.parseLong(kVar2.e));
                try {
                    PushDatabase.insertStatisticsInfo(PushDatabase.getDb(context), kVar2);
                    Log.i(TAG, "insert into db " + context.getPackageName());
                } catch (SQLiteException e2) {
                    Log.e(TAG, "inset into db exception");
                    Editor edit2 = sharedPreferences.edit();
                    edit2.putString("cache_when_exception", kVar2.b + "#" + kVar2.c + "#" + kVar2.d + "#" + kVar2.e + "#" + kVar2.f + "#" + kVar2.g);
                    edit2.commit();
                }
            }
        }
    }

    public static boolean isConnected(Context context) {
        if (isNullContext(context)) {
            return false;
        }
        boolean z = true;
        if (!(m.o(context) && PushSettings.b(context))) {
            z = false;
        }
        return z;
    }

    private static boolean isNullContext(Context context) {
        if (context != null) {
            return false;
        }
        Log.e(TAG, "Context is null!");
        return true;
    }

    public static boolean isPushEnabled(Context context) {
        return (isNullContext(context) || m.c(context)) ? false : true;
    }

    public static void listTags(Context context) {
        if (!isNullContext(context)) {
            Intent createMethodIntent = createMethodIntent(context);
            createMethodIntent.putExtra("method", PushConstants.METHOD_LISTTAGS);
            context.sendBroadcast(createMethodIntent);
        }
    }

    public static void resumeWork(Context context) {
        if (!isNullContext(context)) {
            b.b(context, true);
            m.c(context, true);
            b.a(context, true);
            m.j(context);
            bind(context, 0);
        }
    }

    public static void sendMsgToServer(Context context, String str, String str2, String str3) {
        if (!isNullContext(context)) {
            Intent createMethodIntent = createMethodIntent(context);
            createMethodIntent.putExtra("method", PushConstants.METHOD_SEND_MSG_TO_SERVER);
            createMethodIntent.putExtra(PushConstants.EXTRA_APP_ID, str);
            createMethodIntent.putExtra(PushConstants.EXTRA_CB_URL, str2);
            createMethodIntent.putExtra(PushConstants.EXTRA_MSG, str3);
            context.sendBroadcast(createMethodIntent);
        }
    }

    public static void sendMsgToUser(Context context, String str, String str2, String str3, String str4) {
        if (!isNullContext(context)) {
            Intent createMethodIntent = createMethodIntent(context);
            createMethodIntent.putExtra("method", PushConstants.METHOD_SEND_MSG_TO_USER);
            createMethodIntent.putExtra(PushConstants.EXTRA_APP_ID, str);
            createMethodIntent.putExtra(PushConstants.EXTRA_USER_ID, str2);
            createMethodIntent.putExtra(PushConstants.EXTRA_MSG_KEY, str3);
            createMethodIntent.putExtra(PushConstants.EXTRA_MSG, str4);
            context.sendBroadcast(createMethodIntent);
        }
    }

    public static void setAccessToken(Context context, String str) {
        if (!isNullContext(context)) {
            Editor edit = context.getSharedPreferences(context.getPackageName(), 1).edit();
            edit.putInt("com.baidu.android.pushservice.PushManager.LOGIN_TYPE", 1);
            edit.putString("com.baidu.android.pushservice.PushManager.LONGIN_VALUE", str);
            edit.commit();
        }
    }

    public static void setApiKey(Context context, String str) {
        if (!isNullContext(context)) {
            Editor edit = context.getSharedPreferences(context.getPackageName(), 1).edit();
            edit.putInt("com.baidu.android.pushservice.PushManager.LOGIN_TYPE", 0);
            edit.putString("com.baidu.android.pushservice.PushManager.LONGIN_VALUE", str);
            edit.commit();
        }
    }

    public static void setBduss(Context context, String str) {
        if (!isNullContext(context)) {
            Editor edit = context.getSharedPreferences(context.getPackageName(), 1).edit();
            edit.putInt("com.baidu.android.pushservice.PushManager.LOGIN_TYPE", 2);
            edit.putString("com.baidu.android.pushservice.PushManager.BDUSS", str);
            edit.commit();
        }
    }

    public static void setDefaultNotificationBuilder(Context context, PushNotificationBuilder pushNotificationBuilder) {
        if (!isNullContext(context)) {
            c.a(context, pushNotificationBuilder);
        }
    }

    public static void setMediaNotificationBuilder(Context context, PushNotificationBuilder pushNotificationBuilder) {
        if (!isNullContext(context)) {
            c.b(context, pushNotificationBuilder);
        }
    }

    public static void setNotificationBuilder(Context context, int i, PushNotificationBuilder pushNotificationBuilder) {
        if (!isNullContext(context)) {
            if (i < 1 || i > 1000) {
                Log.e(TAG, "set notification builder error, id is illegal !");
            } else {
                c.a(context, i, pushNotificationBuilder);
            }
        }
    }

    public static void setTags(Context context, List list) {
        if (!isNullContext(context)) {
            if (list == null || list.size() == 0) {
                Log.w(TAG, "No tags specified, do nothing.");
                return;
            }
            String str = "[";
            Iterator it = list.iterator();
            while (true) {
                String str2 = str;
                if (it.hasNext()) {
                    str = ((str2 + "\"") + ((String) it.next())) + "\",";
                } else {
                    str = str2.substring(0, str2.length() - 1) + "]";
                    Intent createMethodIntent = createMethodIntent(context);
                    createMethodIntent.putExtra("method", PushConstants.METHOD_SET_TAGS);
                    createMethodIntent.putExtra(PushConstants.EXTRA_TAGS, str);
                    context.sendBroadcast(createMethodIntent);
                    return;
                }
            }
        }
    }

    public static void startWork(Context context, int i, String str) {
        if (!isNullContext(context)) {
            b.b(context, true);
            Editor edit = context.getSharedPreferences(context.getPackageName(), 0).edit();
            if (i == 1) {
                edit.putInt("com.baidu.android.pushservice.PushManager.LOGIN_TYPE", 1);
                edit.putString("com.baidu.android.pushservice.PushManager.LONGIN_VALUE", str);
            } else if (i == 0) {
                edit.putInt("com.baidu.android.pushservice.PushManager.LOGIN_TYPE", 0);
                edit.putString("com.baidu.android.pushservice.PushManager.LONGIN_VALUE", str);
            } else {
                Log.e(TAG, "Wrong login type, please check!");
                return;
            }
            edit.commit();
            PushSettings.a(context);
            m.j(context);
            bind(context, 0);
        }
    }

    public static void startWork(Context context, String str, String str2) {
        if (!isNullContext(context)) {
            b.b(context, true);
            Editor edit = context.getSharedPreferences(context.getPackageName(), 0).edit();
            edit.putInt("com.baidu.android.pushservice.PushManager.LOGIN_TYPE", 2);
            edit.putString("com.baidu.android.pushservice.PushManager.LONGIN_VALUE", str);
            edit.putString("com.baidu.android.pushservice.PushManager.BDUSS", str2);
            edit.commit();
            PushSettings.a(context);
            m.j(context);
            bind(context, 0);
        }
    }

    public static void stopWork(Context context) {
        if (!isNullContext(context)) {
            b.b(context, false);
            m.c(context, true);
            unbind(context);
            b.a(context, true);
            m.g(context, context.getPackageName());
        }
    }

    public static void tryConnect(Context context) {
        if (!isNullContext(context)) {
            context.sendBroadcast(createMethodIntent(context));
        }
    }

    public static void unbind(Context context) {
        if (!isNullContext(context)) {
            Intent createMethodIntent = createMethodIntent(context);
            createMethodIntent.putExtra("method", PushConstants.METHOD_UNBIND);
            context.sendBroadcast(createMethodIntent);
        }
    }

    public static void unbindGroup(Context context, String str) {
        if (!isNullContext(context)) {
            Intent createMethodIntent = createMethodIntent(context);
            createMethodIntent.putExtra("method", PushConstants.METHOD_GUNBIND);
            createMethodIntent.putExtra(PushConstants.EXTRA_GID, str);
            context.sendBroadcast(createMethodIntent);
        }
    }
}
