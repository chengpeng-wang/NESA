package com.splunk.mint;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import com.splunk.mint.Properties.RemoteSettingsProps;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

class Utils {
    protected static final String CONNECTION = "connection";
    private static final int Debug = 20;
    private static final int Error = 60;
    private static final int Info = 30;
    protected static final String LASTPINGTIME = "LASTPINGTIME";
    protected static final String STATE = "state";
    private static final int Verbose = 10;
    private static final int Warning = 50;
    private static ExecutorService executor = null;
    private static final String forceSendPingFile = ".setForceSendPingOnNextStart";

    Utils() {
    }

    protected static String getRandomSessionNumber() {
        String time = String.valueOf(System.currentTimeMillis());
        return time.substring(time.length() - 8, time.length());
    }

    protected static String getScreenOrientation(Context gContext) {
        String rotation = "NA";
        switch (((WindowManager) gContext.getSystemService("window")).getDefaultDisplay().getOrientation()) {
            case 0:
                return "Portrait";
            case 1:
                return "LandscapeRight";
            case 2:
                return "PortraitUpsideDown";
            case 3:
                return "LandscapeLeft";
            default:
                return rotation;
        }
    }

    protected static final boolean isKitKat() {
        if (VERSION.SDK_INT == 19) {
            return true;
        }
        return false;
    }

    protected static int convertLoggingLevelToInt(MintLogLevel level) {
        if (level.equals(MintLogLevel.Debug)) {
            return 20;
        }
        if (level.equals(MintLogLevel.Error)) {
            return 60;
        }
        if (level.equals(MintLogLevel.Info)) {
            return 30;
        }
        if (level.equals(MintLogLevel.Verbose) || !level.equals(MintLogLevel.Warning)) {
            return 10;
        }
        return 50;
    }

    protected static HashMap<String, String> getConnectionInfo(Context context) {
        HashMap<String, String> infoMap = new HashMap(2);
        infoMap.put(CONNECTION, "NA");
        infoMap.put(STATE, "NA");
        if (context != null) {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                if (Mint.DEBUG) {
                    Logger.logError("PackageManager in CheckNetworkConnection is null!");
                }
            } else if (packageManager.checkPermission("android.permission.ACCESS_NETWORK_STATE", Properties.APP_PACKAGE) == 0) {
                NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                if (info != null) {
                    if (info.getSubtypeName() == null || info.getSubtypeName().length() == 0) {
                        infoMap.put(CONNECTION, info.getTypeName());
                    } else {
                        infoMap.put(CONNECTION, info.getSubtypeName());
                    }
                    infoMap.put(STATE, info.getState().toString());
                } else {
                    infoMap.put(CONNECTION, "No Connection");
                }
            }
        } else if (Mint.DEBUG) {
            Logger.logError("Context in getConnection is null!");
        }
        return infoMap;
    }

    protected static synchronized boolean shouldSendPing(Context ctx) {
        boolean shouldSendPing;
        synchronized (Utils.class) {
            File forcePingfile = new File(Properties.FILES_PATH + "/" + forceSendPingFile);
            if (forcePingfile == null || !forcePingfile.exists()) {
                shouldSendPing = true;
                if (ctx != null) {
                    SharedPreferences preferences = ctx.getSharedPreferences("Mint", 0);
                    if (preferences != null) {
                        shouldSendPing = System.currentTimeMillis() - preferences.getLong(LASTPINGTIME, 0) > ((long) (RemoteSettingsProps.sessionTime.intValue() * 1000));
                        if (shouldSendPing) {
                            setLastPingSentTime(ctx);
                        }
                    }
                }
            } else {
                forcePingfile.delete();
                shouldSendPing = true;
            }
        }
        return shouldSendPing;
    }

    protected static void setForceSendPingOnNextStart() {
        Thread t = new LowPriorityThreadFactory().newThread(new Runnable() {
            public void run() {
                File file = new File(Properties.FILES_PATH + "/" + Utils.forceSendPingFile);
                if (file != null && !file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        ExecutorService executor = getExecutor();
        if (t != null && executor != null) {
            executor.submit(t);
        }
    }

    private static ExecutorService getExecutor() {
        if (executor == null) {
            executor = Executors.newFixedThreadPool(1);
        }
        return executor;
    }

    protected static void setLastPingSentTime(final Context ctx) {
        Thread t = new LowPriorityThreadFactory().newThread(new Runnable() {
            public void run() {
                if (ctx != null) {
                    SharedPreferences preferences = ctx.getSharedPreferences("Mint", 0);
                    if (preferences != null) {
                        preferences.edit().putLong(Utils.LASTPINGTIME, System.currentTimeMillis()).commit();
                    }
                }
            }
        });
        ExecutorService executor = getExecutor();
        if (t != null && executor != null) {
            executor.submit(t);
        }
    }

    protected static void clearLastPingSentTime(final Context ctx) {
        Thread t = new LowPriorityThreadFactory().newThread(new Runnable() {
            public void run() {
                if (ctx != null) {
                    SharedPreferences preferences = ctx.getSharedPreferences("Mint", 0);
                    if (preferences != null) {
                        preferences.edit().putLong(Utils.LASTPINGTIME, 0).commit();
                    }
                }
            }
        });
        ExecutorService executor = getExecutor();
        if (t != null && executor != null) {
            executor.submit(t);
        }
    }

    protected static EnumStateStatus isGPSOn(Context gContext) {
        EnumStateStatus gps_status = EnumStateStatus.ON;
        if (gContext.getPackageManager().checkPermission("android.permission.ACCESS_FINE_LOCATION", Properties.APP_PACKAGE) != 0) {
            return EnumStateStatus.NA;
        }
        if (((LocationManager) gContext.getSystemService("location")).isProviderEnabled("gps")) {
            return gps_status;
        }
        return EnumStateStatus.OFF;
    }

    protected static final String getTime() {
        String time = String.valueOf(System.currentTimeMillis());
        try {
            return String.valueOf(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() / 1000);
        } catch (Exception e) {
            return time;
        }
    }

    protected static boolean checkForRoot() {
        for (String dir : new String[]{"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/", "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"}) {
            if (new File(dir + "su").exists()) {
                return true;
            }
        }
        return false;
    }

    protected static String MD5(String data) throws Exception {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(data.getBytes(), 0, data.length());
        return new BigInteger(1, m.digest()).toString(16);
    }

    protected static final Long getMilisFromStart() {
        return Long.valueOf(System.currentTimeMillis() - Properties.TIMESTAMP);
    }

    protected static final String getCarrier(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService("phone");
        if (manager == null) {
            return "NA";
        }
        String carrier = null;
        if (manager.getSimState() == 5) {
            carrier = manager.getSimOperatorName();
        }
        if (carrier == null || carrier.length() == 0) {
            carrier = manager.getNetworkOperatorName();
        }
        if (carrier == null || carrier.length() == 0) {
            return "NA";
        }
        return carrier;
    }

    protected static String readFile(String filePath) throws Exception {
        Exception e;
        Throwable th;
        if (filePath == null) {
            throw new IllegalArgumentException("filePath Argument is null");
        }
        StringBuilder sb1 = new StringBuilder();
        BufferedReader input = null;
        try {
            BufferedReader input2 = new BufferedReader(new FileReader(filePath));
            while (true) {
                try {
                    String line = input2.readLine();
                    if (line == null) {
                        break;
                    }
                    sb1.append(line);
                } catch (Exception e2) {
                    e = e2;
                    input = input2;
                    try {
                        throw e;
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    input = input2;
                    if (input != null) {
                        try {
                            input.close();
                        } catch (IOException e3) {
                            throw e3;
                        }
                    }
                    throw th;
                }
            }
            if (input2 != null) {
                try {
                    input2.close();
                } catch (IOException e32) {
                    throw e32;
                }
            }
            return sb1.toString();
        } catch (Exception e4) {
            e = e4;
            throw e;
        }
    }

    protected static final String readLogs() {
        int lines = Properties.LOG_LINES;
        if (lines < 0) {
            lines = 100;
        }
        String filter = Properties.LOG_FILTER;
        StringBuilder log = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("logcat -d " + filter).getInputStream()));
            List<String> linesList = new ArrayList();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                linesList.add(line);
            }
            if (linesList.size() == 0) {
                return "You must add the android.permission.READ_LOGS permission to your manifest file!";
            }
            int start = linesList.size() - lines;
            if (start < 0) {
                start = 0;
            }
            for (int index = start; index < linesList.size(); index++) {
                log.append(((String) linesList.get(index)) + "\n");
            }
            return log.toString().replaceAll(Pattern.quote("}{^"), "}{ ^");
        } catch (Exception e) {
            Logger.logError("Error reading logcat output!");
            return e.getMessage();
        }
    }

    protected static final HashMap<String, String> getMemoryInfo() {
        HashMap<String, String> map = new HashMap(2);
        boolean memTotalFound = false;
        boolean memFreeFound = false;
        try {
            InputStream in = new ProcessBuilder(new String[]{"/system/bin/cat", "/proc/meminfo"}).start().getInputStream();
            StringBuilder sb1 = new StringBuilder();
            byte[] re = new byte[1024];
            while (in.read(re) != -1) {
                sb1.append(new String(re));
                String[] lines = sb1.toString().split("kB");
                if (lines.length >= 2) {
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    for (String line : lines) {
                        if (!memTotalFound && line.contains("MemTotal:")) {
                            map.put("memTotal", String.valueOf(decimalFormat.format((double) (Float.valueOf(line.substring(line.indexOf(" "), line.lastIndexOf(" ")).trim()).floatValue() / 1024.0f))));
                            memTotalFound = true;
                        }
                        if (!memFreeFound && line.contains("MemFree:")) {
                            map.put("memFree", String.valueOf(decimalFormat.format((double) (Float.valueOf(line.substring(line.indexOf(" "), line.lastIndexOf(" ")).trim()).floatValue() / 1024.0f))));
                            memFreeFound = true;
                        }
                    }
                    in.close();
                    return map;
                }
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return map;
    }

    protected static boolean allowedToSendData() {
        if (Properties.flushOnlyOverWiFi && !Properties.CONNECTION.equals("WIFI")) {
            return false;
        }
        return true;
    }

    @SuppressLint({"NewApi"})
    protected static String isFSEncrypted(Context context) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
        if (VERSION.SDK_INT <= 11) {
            return "NA";
        }
        if (3 == devicePolicyManager.getStorageEncryptionStatus()) {
            return "true";
        }
        return "false";
    }

    public static int getBatteryLevel(Context context) {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        int level = batteryIntent.getIntExtra("level", -1);
        int scale = batteryIntent.getIntExtra("scale", -1);
        if (level == -1 || scale == -1) {
            return 50;
        }
        return Math.round((((float) level) / ((float) scale)) * 100.0f);
    }
}
