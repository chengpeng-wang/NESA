package exts.whats;

import android.app.Application;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class CustomApplication extends Application {
    private WakeLock mWakeLock = null;
    private WifiLock mWiFiLock = null;

    public void onCreate() {
        super.onCreate();
        this.mWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(1, "MyWakeLock");
        this.mWakeLock.acquire();
        this.mWiFiLock = ((WifiManager) getSystemService("wifi")).createWifiLock(1, "MyWiFiLock");
        this.mWiFiLock.acquire();
    }

    public void onTerminate() {
        if (this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
        }
        if (this.mWiFiLock.isHeld()) {
            this.mWiFiLock.release();
        }
        super.onTerminate();
    }
}
