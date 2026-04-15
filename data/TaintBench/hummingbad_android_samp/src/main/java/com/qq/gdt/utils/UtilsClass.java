package com.qq.gdt.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.android.Laucher.MobulaApplication;
import com.android.Laucher.Se;
import com.android.ad.du.ShowDuAd;
import com.mb.bdapp.util.AppInfoUtils;
import com.nicon.tool.HttpUtils;
import com.tencent.bugly.crashreport.common.strategy.BuglyBroadcastRecevier;
import com.umeng.analytics.MobclickAgent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class UtilsClass {
    /* access modifiers changed from: private|static|final */
    public static final String TAG = UtilsClass.class.toString();
    @SuppressLint({"SimpleDateFormat"})
    static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    public static final boolean ifLogout = true;
    public static boolean isPost = false;
    private static String mUUID = null;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static UtilsClass utilsclass = null;
    public final String ADV_NAME = "adv_name";
    public final String AD_TYPE = "type";
    public final String DEVICE_UNIQUEID = "DEVICE_UNIQUEID";
    private final String DU_REQUESTED_NUM = "du_request_num";
    private final String DU_SHOW_NUM = "du_show_num";
    public final String LIVEAPPKEY = "liveAppkey";
    public final boolean LOG_OUT = true;
    public final String NUM = "num";
    private final String PRE_FILE_NAME_KIN = "ktn";
    public final String RESTORE = "restore";
    public final String SHOW_COUNT = "show_count";
    public final String SHOW_TIME = "show_time";
    public final String T_Time = "millis";
    public final String YEARMONTHDAYMINUTE = "YearMonthDayMinute ";
    public final String YEARMONTHDAYMINUTEHOUR = "YearMonthDayMinuteHour";
    public final String YM = "livePlatformAdCategory";
    public int bCount = 20;
    private Editor editor;
    public int extCount = 20;
    private SharedPreferences mNOiconPreferences;
    public int reqCount = 20;
    public int timeInterval = 3;
    public final String xTime = "Xtime";

    public static UtilsClass getInstance() {
        if (utilsclass == null) {
            synchronized (UtilsClass.class) {
                utilsclass = new UtilsClass();
            }
        }
        return utilsclass;
    }

    public int getLastShowCount(Context context) {
        return this.mNOiconPreferences.getInt("extCount", 20) - getShowCount(context);
    }

    public void reportUmengData(Context mContext, String event, String clickType) {
        Map map = new HashMap();
        map.put("clickType", clickType);
        MobclickAgent.onEvent(mContext, event, map);
    }

    public SharedPreferences getSharedPreferences(Context context) {
        if (this.mNOiconPreferences == null && context != null) {
            this.mNOiconPreferences = context.getSharedPreferences("ktn", 0);
        }
        return this.mNOiconPreferences;
    }

    public void startService(Context context) {
        if (!Utilstools.getInstance().isServiceRunning(context)) {
            Intent intent = new Intent(context, Se.class);
            intent.setFlags(268468224);
            context.startService(intent);
        }
    }

    public static String getNowDay() {
        return formatter.format(new Date());
    }

    public static String getDay(long time) {
        return formatter.format(new Date(time));
    }

    public static String getNow() {
        return sdf.format(new Date());
    }

    public boolean loadMogo(Context context) {
        int maxCount = 20;
        this.mNOiconPreferences = getSharedPreferences(context);
        this.reqCount = this.mNOiconPreferences.getInt("reqCount", 20);
        if (this.reqCount != 20) {
            maxCount = this.reqCount;
        }
        String lastShowTime = this.mNOiconPreferences.getString("show_time", "");
        if (("".equals(lastShowTime) || diffTime(context, new Date(), lastShowTime)) && getShowCount(context) <= maxCount) {
            return true;
        }
        return false;
    }

    public int getShowCount(Context context) {
        this.mNOiconPreferences = getSharedPreferences(context);
        String[] arr = this.mNOiconPreferences.getString("show_count", "").split(",");
        if (getNowDay().equals(arr[0])) {
            return Integer.parseInt(arr[1]);
        }
        return 0;
    }

    public boolean diffTime(Context context, Date d1, String dateStr) {
        int admobTimeInterval = 3;
        try {
            Log.i(TAG, "timeInterval:" + this.timeInterval + ",reqCount:" + this.reqCount);
            this.mNOiconPreferences = getSharedPreferences(context);
            this.timeInterval = this.mNOiconPreferences.getInt("timeInterval", 3);
            if (this.timeInterval != 3) {
                admobTimeInterval = this.timeInterval;
            }
            long s = diffTime(d1, dateStr) / BuglyBroadcastRecevier.UPLOADLIMITED;
            Log.i(TAG, "d1：" + sdf.format(d1));
            Log.i(TAG, "d1：" + dateStr);
            Log.i(TAG, "时间差异:" + s);
            Log.i(TAG, "admobTimeInterval：" + admobTimeInterval);
            if (s < ((long) admobTimeInterval)) {
                return false;
            }
            int interval_fill = admobTimeInterval * 2;
            if (s >= ((long) interval_fill)) {
                Log.e("HDJ", "--- 时间间隔(" + interval_fill + "分钟)，调用fill() ---");
                MobulaApplication.getDuNativeAd().fill();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public long diffMinuts(Date d1, String dateStr) {
        return diffTime(d1, dateStr) / BuglyBroadcastRecevier.UPLOADLIMITED;
    }

    public long diffTime(Date d1, String dateStr) {
        long time2 = 0;
        try {
            if (!TextUtils.isEmpty(dateStr)) {
                time2 = sdf.parse(dateStr).getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d1.getTime() - time2;
    }

    public void recordCount(Context context, boolean countAdd) {
        this.mNOiconPreferences = getSharedPreferences(context);
        int count = 1;
        String nowDay = getNowDay();
        String s = this.mNOiconPreferences.getString("show_count", "");
        this.editor = this.mNOiconPreferences.edit();
        String[] arr = s.split(",");
        if (!isVersionNoChange(context) || !nowDay.equals(arr[0])) {
            Log.e("HDJ", "-------- 每天刷新云控数据 --------");
            ShowDuAd.ignoreLimit = false;
            resetDuNum(context);
            this.mNOiconPreferences.edit().putBoolean("isLoaded", false).commit();
            getAdmobConfig(context, DomainManger.FIRST_DOMAIN);
        } else if (arr.length > 1) {
            count = countAdd ? Integer.parseInt(arr[1]) + 1 : Integer.parseInt(arr[1]);
        }
        this.editor.putString("show_count", new StringBuilder(String.valueOf(nowDay)).append(",").append(count).toString()).commit();
        String showTime = sdf.format(new Date());
        Log.i(TAG, "admob广告展示时间：" + showTime);
        this.editor.putString("show_time", showTime).commit();
    }

    private boolean isVersionNoChange(Context context) {
        String spfVersion = getSharedPreferences(context).getString("spVersion", "null");
        String appVersion = getCurrentVersion(context);
        Log.e("HDJ  ----- 云控上报活跃  版本核对 ----", new StringBuilder(String.valueOf(spfVersion)).append("  /  ").append(appVersion).toString());
        return "null".equalsIgnoreCase(spfVersion) || appVersion.equalsIgnoreCase(spfVersion);
    }

    public String getCurrentVersion(Context context) {
        String curVersionName = "";
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
            return curVersionName;
        }
    }

    public void getAdmobConfig(final Context context, String domain) {
        final SharedPreferences sp = getSharedPreferences(context);
        final String URLpath = DomainManger.getInstance().getDomainUrl(domain);
        Log.i(TAG, " URLpath:" + URLpath + ",failCount:" + DomainManger.getInstance().getFailCount());
        if (!sp.getBoolean("isLoaded", false)) {
            new Thread() {
                public void run() {
                    try {
                        ArrayList<NameValuePair> params = new ArrayList();
                        params.add(new BasicNameValuePair("mid", new StringBuilder(String.valueOf(UtilsClass.this.getDevicdId(context))).toString()));
                        params.add(new BasicNameValuePair("verno", new StringBuilder(String.valueOf(UtilsClass.this.getCurrentVersion(context))).toString()));
                        params.add(new BasicNameValuePair("cid", new StringBuilder(String.valueOf(UtilsClass.this.getChannelId(context))).toString()));
                        params.add(new BasicNameValuePair("android_id", AppInfoUtils.getAndroidId(context)));
                        String result = HttpUtils.getInstance().httpPost(URLpath, params);
                        Log.e("HDJ-- 云控查询 --", URLpath + "?mid=" + UtilsClass.this.getDevicdId(context) + "&verno=" + UtilsClass.this.getCurrentVersion(context) + "&cid=" + UtilsClass.this.getChannelId(context) + "&android_id=" + AppInfoUtils.getAndroidId(context));
                        Log.e("HDJ-- 云控结果 --", result);
                        Log.i(UtilsClass.TAG, " getAdmobConfig mid:" + UtilsClass.this.getDevicdId(context) + ",verno:" + UtilsClass.this.getCurrentVersion(context) + ",advName:" + "cheetah,mv" + ", result:" + result);
                        JSONObject jo = new JSONObject(result);
                        if (jo.getBoolean("success")) {
                            JSONObject data = jo.optJSONObject("result");
                            UtilsClass.this.timeInterval = data.getInt("timeinterval");
                            UtilsClass.this.reqCount = data.getInt("displaytimes");
                            UtilsClass.this.extCount = data.getInt("realexits");
                            UtilsClass.this.bCount = data.getInt("subscripttimes");
                            String advName = data.getString("advName");
                            String adRequestTimes = data.getString("advRequestTimes");
                            String adShowTimes = data.getString("advDisplayTimes");
                            Log.e("HDJ -- 获取到云控聚合次数 --  -- (请求/展示) --", new StringBuilder(String.valueOf(adRequestTimes)).append("/").append(adShowTimes).toString());
                            Log.i(UtilsClass.TAG, " success timeInterval:" + UtilsClass.this.timeInterval + ",reqCount:" + UtilsClass.this.reqCount);
                            Editor editor = sp.edit();
                            editor.putInt("reqCount", UtilsClass.this.reqCount);
                            editor.putInt("timeInterval", UtilsClass.this.timeInterval);
                            editor.putInt("extCount", UtilsClass.this.extCount);
                            editor.putInt("bCount", UtilsClass.this.bCount);
                            editor.putBoolean("isLoaded", true);
                            editor.putString("adv_name", advName);
                            editor.putString("spVersion", UtilsClass.this.getCurrentVersion(context));
                            editor.putString("adRequestTimes", adRequestTimes);
                            editor.putString("adShowTimes", adShowTimes);
                            editor.commit();
                            UtilsClass.this.reportUmengEvent(context, "ADV_NAME_CLOUD", UtilsClass.this.getCurrentVersion(context));
                        }
                        DomainManger.getInstance().setFailCount(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if ((e instanceof UnknownHostException) || (e.getCause() instanceof UnknownHostException)) {
                            DomainManger.getInstance().getDomainAndrequest(URLpath, context);
                        }
                    }
                }
            }.start();
        }
    }

    public int[] getCludShowCounts(Context context) {
        if (this.mNOiconPreferences == null) {
            this.mNOiconPreferences = getSharedPreferences(context);
        }
        return parseStr2Ints(this.mNOiconPreferences.getString("adShowTimes", ""));
    }

    public int[] getCludRequestCounts(Context context) {
        if (this.mNOiconPreferences == null) {
            this.mNOiconPreferences = getSharedPreferences(context);
        }
        return parseStr2Ints(this.mNOiconPreferences.getString("adRequestTimes", ""));
    }

    private int[] parseStr2Ints(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.contains(",")) {
            String[] numStr = str.split(",");
            int[] counts = new int[numStr.length];
            for (int i = 0; i < numStr.length; i++) {
                counts[i] = Integer.parseInt(numStr[i]);
            }
            return counts;
        }
        return new int[]{Integer.parseInt(str)};
    }

    public String getMogoKey(Context context) {
        Bundle metaData = null;
        String mogoKey = "";
        if (context == null) {
            return "";
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (ai != null) {
                metaData = ai.metaData;
            }
            if (metaData != null) {
                mogoKey = metaData.getString("MOGO_KEY");
            }
        } catch (NameNotFoundException e) {
        }
        return mogoKey;
    }

    public int getChannelId(Context context) {
        Bundle metaData = null;
        int apiKey = 0;
        if (context == null) {
            return 0;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (ai != null) {
                metaData = ai.metaData;
            }
            if (metaData != null) {
                apiKey = metaData.getInt("CHANNEL_ID");
            }
        } catch (NameNotFoundException e) {
        }
        return apiKey;
    }

    public String getDevicdId(Context context) {
        this.mNOiconPreferences = getSharedPreferences(context);
        if (mUUID == null) {
            String devId = this.mNOiconPreferences.getString("DEVICE_UNIQUEID", "");
            if (devId == null || devId.equals("")) {
                devId = generateUUID(context);
                this.editor = this.mNOiconPreferences.edit();
                this.editor.putString("DEVICE_UNIQUEID", devId);
                this.editor.commit();
            }
            mUUID = devId;
        }
        return mUUID;
    }

    @SuppressLint({"NewApi"})
    public static final String generateUUID(Context context) {
        String s2;
        String s = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        if (s == null) {
            s = "";
        }
        String s1 = Secure.getString(context.getContentResolver(), "android_id");
        if (s1 == null) {
            s1 = "";
        }
        if (VERSION.SDK_INT >= 9) {
            s2 = Build.SERIAL;
            if (s2 == null) {
                s2 = "";
            }
        } else {
            s2 = getDeviceSerial();
        }
        try {
            return getMD5String(s + s1 + s2);
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            nosuchalgorithmexception.printStackTrace();
            return null;
        }
    }

    private static final String getMD5String(String s) throws NoSuchAlgorithmException {
        byte[] abyte0 = MessageDigest.getInstance("SHA-1").digest(s.getBytes());
        Formatter formatter = new Formatter();
        int i = abyte0.length;
        for (int j = 0; j < i; j++) {
            formatter.format("%02x", new Object[]{Byte.valueOf(abyte0[j])});
        }
        String md5s = formatter.toString();
        formatter.close();
        return md5s;
    }

    private static final String getDeviceSerial() {
        try {
            Method method = Class.forName("android.os.Build").getDeclaredMethod("getString", new Class[]{Class.forName("java.lang.String")});
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            return (String) method.invoke(new Build(), new Object[]{"ro.serialno"});
        } catch (ClassNotFoundException classnotfoundexception) {
            classnotfoundexception.printStackTrace();
            return "";
        } catch (NoSuchMethodException nosuchmethodexception) {
            nosuchmethodexception.printStackTrace();
            return "";
        } catch (InvocationTargetException invocationtargetexception) {
            invocationtargetexception.printStackTrace();
            return "";
        } catch (IllegalAccessException illegalaccessexception) {
            illegalaccessexception.printStackTrace();
            return "";
        }
    }

    public static final String E(String s) {
        return new String(Base64.decode(s, 0));
    }

    public void reportUmengEvent(Context mContext, String event, String clickType) {
        Map map = new HashMap();
        map.put("clickType", clickType);
        MobclickAgent.onEvent(mContext, event, map);
    }

    public int getDuRequestedNum(Context context) {
        this.mNOiconPreferences = getSharedPreferences(context);
        return this.mNOiconPreferences.getInt("du_request_num", 0);
    }

    public int getDuShowedNum(Context context) {
        this.mNOiconPreferences = getSharedPreferences(context);
        return this.mNOiconPreferences.getInt("du_show_num", 0);
    }

    public void markDuShowed(Context context) {
        this.mNOiconPreferences = getSharedPreferences(context);
        this.mNOiconPreferences.edit().putInt("du_show_num", getDuShowedNum(context) + 1).commit();
    }

    public void markDuRequested(Context context) {
        this.mNOiconPreferences = getSharedPreferences(context);
        this.mNOiconPreferences.edit().putInt("du_request_num", getDuRequestedNum(context) + 1).commit();
    }

    public void resetDuNum(Context context) {
        this.mNOiconPreferences = getSharedPreferences(context);
        this.mNOiconPreferences.edit().putInt("du_request_num", 0).commit();
        this.mNOiconPreferences.edit().putInt("du_show_num", 0).commit();
    }
}
