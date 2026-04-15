package com.splunk.mint;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Build.VERSION;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

public class Properties {
    static String API_KEY = "NA";
    protected static final String API_VERSION = "1";
    static String APP_PACKAGE = "NA";
    static String APP_VERSIONCODE = "NA";
    static String APP_VERSIONNAME = "NA";
    static int BATTERY_LEVEL = 50;
    static String CARRIER = "NA";
    static String CONNECTION = "NA";
    static String FILES_PATH = null;
    static boolean HAS_ROOT = false;
    static String ISFSENCRYPTED = "NA";
    static EnumStateStatus IS_GPS_ON = EnumStateStatus.NA;
    static String LOCALE = "NA";
    static String LOG_FILTER = "";
    static int LOG_LINES = 500;
    static String OS_VERSION = "NA";
    static String PHONE_BRAND = null;
    static String PHONE_MODEL = "NA";
    static final String REMOTEIP_PLACEHOLDER = "{%#@@#%}";
    public static final String REST_VERSION = "1.0";
    static String SCREEN_ORIENTATION = "NA";
    protected static final String SDK_PLATFORM = "Android";
    protected static final String SDK_VERSION = "4.2.1";
    static boolean SEND_LOG = false;
    static String STATE = "NA";
    static final String TAG = "Mint";
    static long TIMESTAMP = 0;
    static String UID = "";
    public static boolean USER_OPTEDOUT = false;
    static BreadcrumbsLimited breadcrumbs = new BreadcrumbsLimited();
    private static final String[] defaultExcludedUrls = new String[]{"splkmobile.com"};
    public static final ExcludedUrls excludedUrls = new ExcludedUrls(defaultExcludedUrls);
    static ExtraData extraData = new ExtraData();
    public static boolean flushOnlyOverWiFi = false;
    private static boolean initialized = false;
    public static boolean isKitKat = false;
    public static long lastPingTime = 0;
    public static String lastView = "NA";
    public static long lastViewTime = 0;
    private static MintUrls mintUrls = null;
    static boolean proxyEnabled = false;
    static boolean sendOnlyWiFi = false;
    public static ArrayList<String> transactions = new ArrayList(2);
    static volatile TransactionsDatabase transactionsDatabase = new TransactionsDatabase();
    static String userIdentifier = "NA";

    public static class RemoteSettingsProps {
        static JSONObject devSettings = new JSONObject();
        static Integer eventLevel = Integer.valueOf(Utils.convertLoggingLevelToInt(MintLogLevel.Verbose));
        static String hashCode = "none";
        static Integer logLevel = Integer.valueOf(Utils.convertLoggingLevelToInt(MintLogLevel.Verbose));
        static Boolean netMonitoringEnabled = Boolean.valueOf(true);
        static Integer sessionTime = Integer.valueOf(60);

        public static String toReadableFormat() {
            return "loglevel: " + String.valueOf(logLevel) + " eventLevel: " + String.valueOf(eventLevel) + " netMonitoring: " + String.valueOf(netMonitoringEnabled) + " sessionTime: " + String.valueOf(sessionTime) + " devSettings: " + devSettings.toString() + " hashCode: " + hashCode;
        }
    }

    public static boolean isPluginInitialized() {
        if (!initialized) {
            Logger.logWarning("Mint SDK is not initialized!");
        }
        return initialized;
    }

    protected static void initialize(Context context, String url, String apiKey) {
        if (!initialized) {
            UID = UidManager.getUid(context);
            if (mintUrls == null && apiKey != null) {
                mintUrls = new MintUrls(url, apiKey);
            }
            try {
                PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                APP_VERSIONNAME = pi.versionName;
                APP_VERSIONCODE = String.valueOf(pi.versionCode);
                APP_PACKAGE = pi.packageName;
            } catch (Exception e) {
                Logger.logError("Error collecting information about the package!");
                if (Mint.DEBUG) {
                    e.printStackTrace();
                }
            }
            PHONE_MODEL = Build.MODEL;
            PHONE_BRAND = Build.MANUFACTURER;
            OS_VERSION = VERSION.RELEASE;
            HAS_ROOT = Utils.checkForRoot();
            ISFSENCRYPTED = Utils.isFSEncrypted(context);
            BATTERY_LEVEL = Utils.getBatteryLevel(context);
            isKitKat = Utils.isKitKat();
            if (breadcrumbs == null) {
                breadcrumbs = new BreadcrumbsLimited();
            }
            if (extraData == null) {
                extraData = new ExtraData();
            }
            if (transactionsDatabase == null) {
                transactionsDatabase = new TransactionsDatabase();
            }
            try {
                FILES_PATH = context.getFilesDir().getAbsolutePath();
            } catch (Exception e2) {
                if (Mint.DEBUG) {
                    e2.printStackTrace();
                }
            }
            RemoteSettingsData remoteSettings = RemoteSettings.loadRemoteSettings(context);
            if (remoteSettings != null) {
                RemoteSettingsProps.logLevel = remoteSettings.logLevel;
                RemoteSettingsProps.eventLevel = remoteSettings.eventLevel;
                RemoteSettingsProps.netMonitoringEnabled = remoteSettings.netMonitoring;
                RemoteSettingsProps.sessionTime = remoteSettings.sessionTime;
                RemoteSettingsProps.hashCode = remoteSettings.hashCode;
                try {
                    RemoteSettingsProps.devSettings = new JSONObject(remoteSettings.devSettings);
                } catch (JSONException e3) {
                    e3.printStackTrace();
                }
            }
            initialized = true;
        }
        IS_GPS_ON = Utils.isGPSOn(context);
        LOCALE = Locale.getDefault().getCountry();
        if (LOCALE == null || LOCALE.length() == 0) {
            LOCALE = "NA";
        }
        CARRIER = Utils.getCarrier(context);
        SCREEN_ORIENTATION = Utils.getScreenOrientation(context);
        HashMap<String, String> conInfo = Utils.getConnectionInfo(context);
        CONNECTION = (String) conInfo.get("connection");
        STATE = (String) conInfo.get("state");
    }

    protected static final String getSeparator(EnumActionType actionName) {
        return getSeparator(actionName, Utils.getTime());
    }

    protected static final String getSeparator(EnumActionType actionName, String timestamp) {
        return "{^1^" + actionName.toString() + "^" + timestamp + "}";
    }
}
