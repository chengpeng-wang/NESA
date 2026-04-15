package com.qqmagic;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import defpackage.LogCatBroadcaster;
import qqkj.qqmagic.R;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle bundle) {
        Bundle bundle2 = bundle;
        LogCatBroadcaster.start(this);
        super.onCreate(bundle2);
        setContentView(R.layout.main);
        Intent intent = r6;
        Intent intent2 = intent2;
        try {
            intent2 = new Intent(this, Class.forName("com.qqmagic.s"));
            ComponentName startService = startService(intent);
        } catch (ClassNotFoundException e) {
            Throwable th = e;
            NoClassDefFoundError noClassDefFoundError = r12;
            NoClassDefFoundError noClassDefFoundError2 = new NoClassDefFoundError(th.getMessage());
            throw noClassDefFoundError;
        }
    }

    public void lock(View view) {
        View view2 = view;
        Intent intent = r6;
        Intent intent2 = intent2;
        try {
            intent2 = new Intent(this, Class.forName("com.qqmagic.s"));
            ComponentName startService = startService(intent);
        } catch (ClassNotFoundException e) {
            Throwable th = e;
            NoClassDefFoundError noClassDefFoundError = r12;
            NoClassDefFoundError noClassDefFoundError2 = new NoClassDefFoundError(th.getMessage());
            throw noClassDefFoundError;
        }
    }

    public MainActivity() {
    }
}
