package com.address.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
    public static Boolean _noservice = Boolean.valueOf(false);
    private MainActivity _activity = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.init();
        this._activity = this;
        if (!_noservice.booleanValue()) {
            startService(new Intent(this, RunService.class));
        }
        Log.write("Starting MainActivity");
        finish();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        setTitle(Consts.activityName);
    }
}
