package com.mopub.common.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.os.Build.VERSION;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.mopub.common.CreativeOrientation;
import com.mopub.common.Preconditions.NoThrow;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Reflection.MethodBuilder;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import org.apache.http.conn.util.InetAddressUtils;

public class DeviceUtils {
    private static final int MAX_DISK_CACHE_SIZE = 104857600;
    private static final int MAX_MEMORY_CACHE_SIZE = 31457280;
    private static final int MIN_DISK_CACHE_SIZE = 31457280;

    public enum ForceOrientation {
        FORCE_PORTRAIT("portrait"),
        FORCE_LANDSCAPE("landscape"),
        DEVICE_ORIENTATION("device"),
        UNDEFINED("");
        
        @NonNull
        private final String mKey;

        private ForceOrientation(String key) {
            this.mKey = key;
        }

        @NonNull
        public static ForceOrientation getForceOrientation(@Nullable String key) {
            for (ForceOrientation forceOrientation : values()) {
                if (forceOrientation.mKey.equalsIgnoreCase(key)) {
                    return forceOrientation;
                }
            }
            return UNDEFINED;
        }
    }

    public enum IP {
        IPv4,
        IPv6;

        /* access modifiers changed from: private */
        public boolean matches(String address) {
            switch (this) {
                case IPv4:
                    return InetAddressUtils.isIPv4Address(address);
                case IPv6:
                    return InetAddressUtils.isIPv6Address(address);
                default:
                    return false;
            }
        }

        /* access modifiers changed from: private */
        public String toString(String address) {
            switch (this) {
                case IPv4:
                    return address;
                case IPv6:
                    return address.split("%")[0];
                default:
                    return null;
            }
        }
    }

    private DeviceUtils() {
    }

    public static String getIpAddress(IP ip) throws SocketException {
        Iterator it = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
        while (it.hasNext()) {
            Iterator it2 = Collections.list(((NetworkInterface) it.next()).getInetAddresses()).iterator();
            while (it2.hasNext()) {
                InetAddress inetAddress = (InetAddress) it2.next();
                if (!inetAddress.isLoopbackAddress()) {
                    String toUpperCase = inetAddress.getHostAddress().toUpperCase(Locale.US);
                    if (ip.matches(toUpperCase)) {
                        return ip.toString(toUpperCase);
                    }
                }
            }
        }
        return null;
    }

    public static String getHashedUdid(Context context) {
        if (context == null) {
            return null;
        }
        return Utils.sha1(Secure.getString(context.getContentResolver(), "android_id"));
    }

    public static boolean isNetworkAvailable(@Nullable Context context) {
        if (context == null) {
            return false;
        }
        if (context.checkCallingOrSelfPermission("android.permission.INTERNET") == -1) {
            return false;
        }
        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == -1) {
            return true;
        }
        try {
            return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo().isConnected();
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static int memoryCacheSizeBytes(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        long memoryClass = (long) activityManager.getMemoryClass();
        if (VersionCode.currentApiLevel().isAtLeast(VersionCode.HONEYCOMB)) {
            try {
                long intValue;
                if (Utils.bitMaskContainsFlag(context.getApplicationInfo().flags, ApplicationInfo.class.getDeclaredField("FLAG_LARGE_HEAP").getInt(null))) {
                    intValue = (long) ((Integer) new MethodBuilder(activityManager, "getLargeMemoryClass").execute()).intValue();
                } else {
                    intValue = memoryClass;
                }
                memoryClass = intValue;
            } catch (Exception e) {
                MoPubLog.d("Unable to reflectively determine large heap size on Honeycomb and above.");
            }
        }
        return (int) Math.min(31457280, ((memoryClass / 8) * 1024) * 1024);
    }

    public static long diskCacheSizeBytes(File dir, long minSize) {
        try {
            StatFs statFs = new StatFs(dir.getAbsolutePath());
            minSize = (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())) / 50;
        } catch (IllegalArgumentException e) {
            MoPubLog.d("Unable to calculate 2% of available disk space, defaulting to minimum");
        }
        return Math.max(Math.min(minSize, 104857600), 31457280);
    }

    public static long diskCacheSizeBytes(File dir) {
        return diskCacheSizeBytes(dir, 31457280);
    }

    public static int getScreenOrientation(@NonNull Activity activity) {
        return getScreenOrientationFromRotationAndOrientation(activity.getWindowManager().getDefaultDisplay().getRotation(), activity.getResources().getConfiguration().orientation);
    }

    static int getScreenOrientationFromRotationAndOrientation(int rotation, int orientation) {
        if (1 == orientation) {
            switch (rotation) {
                case 1:
                case 2:
                    return 9;
                default:
                    return 1;
            }
        } else if (2 == orientation) {
            switch (rotation) {
                case 2:
                case 3:
                    return 8;
                default:
                    return 0;
            }
        } else {
            MoPubLog.d("Unknown screen orientation. Defaulting to portrait.");
            return 9;
        }
    }

    public static void lockOrientation(@NonNull Activity activity, @NonNull CreativeOrientation creativeOrientation) {
        if (NoThrow.checkNotNull(creativeOrientation) && NoThrow.checkNotNull(activity)) {
            int screenOrientationFromRotationAndOrientation = getScreenOrientationFromRotationAndOrientation(((WindowManager) activity.getSystemService("window")).getDefaultDisplay().getRotation(), activity.getResources().getConfiguration().orientation);
            if (CreativeOrientation.PORTRAIT == creativeOrientation) {
                if (9 == screenOrientationFromRotationAndOrientation) {
                    screenOrientationFromRotationAndOrientation = 9;
                } else {
                    screenOrientationFromRotationAndOrientation = 1;
                }
            } else if (CreativeOrientation.LANDSCAPE != creativeOrientation) {
                return;
            } else {
                if (8 == screenOrientationFromRotationAndOrientation) {
                    screenOrientationFromRotationAndOrientation = 8;
                } else {
                    screenOrientationFromRotationAndOrientation = 0;
                }
            }
            activity.setRequestedOrientation(screenOrientationFromRotationAndOrientation);
        }
    }

    @TargetApi(17)
    public static Point getDeviceDimensions(@NonNull Context context) {
        Integer valueOf;
        Integer valueOf2;
        Throwable e;
        DisplayMetrics displayMetrics;
        if (VERSION.SDK_INT >= 13) {
            Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            if (VERSION.SDK_INT >= 17) {
                Point point = new Point();
                defaultDisplay.getRealSize(point);
                valueOf = Integer.valueOf(point.x);
                valueOf2 = Integer.valueOf(point.y);
            } else {
                try {
                    valueOf2 = (Integer) new MethodBuilder(defaultDisplay, "getRawWidth").execute();
                    try {
                        Integer num = (Integer) new MethodBuilder(defaultDisplay, "getRawHeight").execute();
                        valueOf = valueOf2;
                        valueOf2 = num;
                    } catch (Exception e2) {
                        Throwable th = e2;
                        valueOf = valueOf2;
                        e = th;
                        MoPubLog.v("Display#getRawWidth/Height failed.", e);
                        valueOf2 = null;
                        displayMetrics = context.getResources().getDisplayMetrics();
                        valueOf = Integer.valueOf(displayMetrics.widthPixels);
                        valueOf2 = Integer.valueOf(displayMetrics.heightPixels);
                        return new Point(valueOf.intValue(), valueOf2.intValue());
                    }
                } catch (Exception e3) {
                    e = e3;
                    valueOf = null;
                    MoPubLog.v("Display#getRawWidth/Height failed.", e);
                    valueOf2 = null;
                    displayMetrics = context.getResources().getDisplayMetrics();
                    valueOf = Integer.valueOf(displayMetrics.widthPixels);
                    valueOf2 = Integer.valueOf(displayMetrics.heightPixels);
                    return new Point(valueOf.intValue(), valueOf2.intValue());
                }
            }
        }
        valueOf2 = null;
        valueOf = null;
        if (valueOf == null || r0 == null) {
            displayMetrics = context.getResources().getDisplayMetrics();
            valueOf = Integer.valueOf(displayMetrics.widthPixels);
            valueOf2 = Integer.valueOf(displayMetrics.heightPixels);
        }
        return new Point(valueOf.intValue(), valueOf2.intValue());
    }
}
