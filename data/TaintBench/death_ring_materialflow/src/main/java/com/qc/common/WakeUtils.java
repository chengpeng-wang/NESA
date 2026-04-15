package com.qc.common;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class WakeUtils {
    public static void TestPower(Context context) {
        WakeLock wake = ((PowerManager) context.getSystemService("power")).newWakeLock(1, "zc");
        wake.acquire();
        wake.release();
    }
}
