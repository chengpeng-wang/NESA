package com.mopub.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import com.mopub.common.Preconditions.NoThrow;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.DeviceUtils;
import com.mopub.common.util.Dips;
import com.mopub.common.util.Utils;
import java.util.Locale;

public class ClientMetadata {
    private static final String DEVICE_ORIENTATION_LANDSCAPE = "l";
    private static final String DEVICE_ORIENTATION_PORTRAIT = "p";
    private static final String DEVICE_ORIENTATION_SQUARE = "s";
    private static final String DEVICE_ORIENTATION_UNKNOWN = "u";
    private static final String IFA_PREFIX = "ifa:";
    private static final String SHA_PREFIX = "sha:";
    private static final int TYPE_ETHERNET = 9;
    private static final int UNKNOWN_NETWORK = -1;
    private static volatile ClientMetadata sInstance;
    private boolean mAdvertisingInfoSet = false;
    private String mAppName;
    private final String mAppPackageName;
    private final String mAppVersion;
    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private final String mDeviceManufacturer;
    private final String mDeviceModel;
    private final String mDeviceOsVersion;
    private final String mDeviceProduct;
    private boolean mDoNotTrack = false;
    private final String mIsoCountryCode;
    private final String mNetworkOperator;
    private String mNetworkOperatorForUrl;
    private String mNetworkOperatorName;
    private final String mSdkVersion;
    private final String mSimIsoCountryCode;
    private String mSimOperator;
    private String mSimOperatorName;
    private String mUdid;

    public enum MoPubNetworkType {
        UNKNOWN(0),
        ETHERNET(1),
        WIFI(2),
        MOBILE(3);
        
        private final int mId;

        private MoPubNetworkType(int id) {
            this.mId = id;
        }

        public final String toString() {
            return Integer.toString(this.mId);
        }

        /* access modifiers changed from: private|static */
        public static MoPubNetworkType fromAndroidNetworkType(int type) {
            switch (type) {
                case 0:
                case 2:
                case 3:
                case 4:
                case 5:
                    return MOBILE;
                case 1:
                    return WIFI;
                case 9:
                    return ETHERNET;
                default:
                    return UNKNOWN;
            }
        }

        public final int getId() {
            return this.mId;
        }
    }

    public static ClientMetadata getInstance(Context context) {
        ClientMetadata clientMetadata = sInstance;
        if (clientMetadata == null) {
            synchronized (ClientMetadata.class) {
                clientMetadata = sInstance;
                if (clientMetadata == null) {
                    clientMetadata = new ClientMetadata(context);
                    sInstance = clientMetadata;
                }
            }
        }
        return clientMetadata;
    }

    public static ClientMetadata getInstance() {
        ClientMetadata clientMetadata = sInstance;
        if (clientMetadata == null) {
            synchronized (ClientMetadata.class) {
                clientMetadata = sInstance;
            }
        }
        return clientMetadata;
    }

    public ClientMetadata(Context context) {
        ApplicationInfo applicationInfo;
        this.mContext = context.getApplicationContext();
        this.mConnectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        this.mDeviceManufacturer = Build.MANUFACTURER;
        this.mDeviceModel = Build.MODEL;
        this.mDeviceProduct = Build.PRODUCT;
        this.mDeviceOsVersion = VERSION.RELEASE;
        this.mSdkVersion = "4.1.0";
        this.mAppVersion = getAppVersionFromContext(this.mContext);
        PackageManager packageManager = this.mContext.getPackageManager();
        this.mAppPackageName = context.getPackageName();
        try {
            applicationInfo = packageManager.getApplicationInfo(this.mAppPackageName, 0);
        } catch (NameNotFoundException e) {
            applicationInfo = null;
        }
        if (applicationInfo != null) {
            this.mAppName = (String) packageManager.getApplicationLabel(applicationInfo);
        }
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.mNetworkOperatorForUrl = telephonyManager.getNetworkOperator();
        this.mNetworkOperator = telephonyManager.getNetworkOperator();
        if (telephonyManager.getPhoneType() == 2 && telephonyManager.getSimState() == 5) {
            this.mNetworkOperatorForUrl = telephonyManager.getSimOperator();
            this.mSimOperator = telephonyManager.getSimOperator();
        }
        this.mIsoCountryCode = telephonyManager.getNetworkCountryIso();
        this.mSimIsoCountryCode = telephonyManager.getSimCountryIso();
        try {
            this.mNetworkOperatorName = telephonyManager.getNetworkOperatorName();
            if (telephonyManager.getSimState() == 5) {
                this.mSimOperatorName = telephonyManager.getSimOperatorName();
            }
        } catch (SecurityException e2) {
            this.mNetworkOperatorName = null;
            this.mSimOperatorName = null;
        }
        this.mUdid = getDeviceIdFromContext(this.mContext);
    }

    private static String getAppVersionFromContext(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            MoPubLog.d("Failed to retrieve PackageInfo#versionName.");
            return null;
        }
    }

    private static String getDeviceIdFromContext(Context context) {
        String string = Secure.getString(context.getContentResolver(), "android_id");
        return new StringBuilder(SHA_PREFIX).append(string == null ? "" : Utils.sha1(string)).toString();
    }

    public String getOrientationString() {
        int i = this.mContext.getResources().getConfiguration().orientation;
        String str = DEVICE_ORIENTATION_UNKNOWN;
        if (i == 1) {
            return DEVICE_ORIENTATION_PORTRAIT;
        }
        if (i == 2) {
            return DEVICE_ORIENTATION_LANDSCAPE;
        }
        if (i == 3) {
            return DEVICE_ORIENTATION_SQUARE;
        }
        return str;
    }

    public MoPubNetworkType getActiveNetworkType() {
        int i = -1;
        if (this.mContext.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == 0) {
            NetworkInfo activeNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                i = activeNetworkInfo.getType();
            }
        }
        return MoPubNetworkType.fromAndroidNetworkType(i);
    }

    public float getDensity() {
        return this.mContext.getResources().getDisplayMetrics().density;
    }

    public String getNetworkOperatorForUrl() {
        return this.mNetworkOperatorForUrl;
    }

    public String getNetworkOperator() {
        return this.mNetworkOperator;
    }

    public Locale getDeviceLocale() {
        return this.mContext.getResources().getConfiguration().locale;
    }

    public String getSimOperator() {
        return this.mSimOperator;
    }

    public String getIsoCountryCode() {
        return this.mIsoCountryCode;
    }

    public String getSimIsoCountryCode() {
        return this.mSimIsoCountryCode;
    }

    public String getNetworkOperatorName() {
        return this.mNetworkOperatorName;
    }

    public String getSimOperatorName() {
        return this.mSimOperatorName;
    }

    public synchronized String getDeviceId() {
        return this.mUdid;
    }

    public synchronized boolean isDoNotTrackSet() {
        return this.mDoNotTrack;
    }

    public synchronized void setAdvertisingInfo(String advertisingId, boolean doNotTrack) {
        this.mUdid = new StringBuilder(IFA_PREFIX).append(advertisingId).toString();
        this.mDoNotTrack = doNotTrack;
        this.mAdvertisingInfoSet = true;
    }

    public synchronized boolean isAdvertisingInfoSet() {
        return this.mAdvertisingInfoSet;
    }

    public String getDeviceManufacturer() {
        return this.mDeviceManufacturer;
    }

    public String getDeviceModel() {
        return this.mDeviceModel;
    }

    public String getDeviceProduct() {
        return this.mDeviceProduct;
    }

    public String getDeviceOsVersion() {
        return this.mDeviceOsVersion;
    }

    public int getDeviceScreenWidthDip() {
        return Dips.screenWidthAsIntDips(this.mContext);
    }

    public int getDeviceScreenHeightDip() {
        return Dips.screenHeightAsIntDips(this.mContext);
    }

    public Point getDeviceDimensions() {
        if (NoThrow.checkNotNull(this.mContext)) {
            return DeviceUtils.getDeviceDimensions(this.mContext);
        }
        return new Point(0, 0);
    }

    public String getSdkVersion() {
        return this.mSdkVersion;
    }

    public String getAppVersion() {
        return this.mAppVersion;
    }

    public String getAppPackageName() {
        return this.mAppPackageName;
    }

    public String getAppName() {
        return this.mAppName;
    }

    @Deprecated
    @VisibleForTesting
    public static void setInstance(ClientMetadata clientMetadata) {
        synchronized (ClientMetadata.class) {
            sInstance = clientMetadata;
        }
    }

    @VisibleForTesting
    public static void clearForTesting() {
        sInstance = null;
    }
}
