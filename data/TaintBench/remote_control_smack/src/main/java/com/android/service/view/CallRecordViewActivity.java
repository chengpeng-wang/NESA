package com.android.service.view;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.android.service.MainControlActivity;
import com.android.service.R;
import com.xmpp.client.util.AppPreferences;
import com.xmpp.client.util.PresenceType;
import com.xmpp.client.util.PushDialogModle;
import com.xmpp.client.util.ValueUtiles;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class CallRecordViewActivity extends Activity {
    public static Button btn_callrecord_command;
    public static Button btn_callrecord_update;
    public static Context callrecord_ctx;
    public static ListView callrecord_lv;
    public static TextView callrecord_update_time;
    /* access modifiers changed from: private */
    public AppPreferences _appPrefs;
    /* access modifiers changed from: private */
    public CallRecordDataBean callrecorddata;
    /* access modifiers changed from: private */
    public String hostname = "";
    /* access modifiers changed from: private */
    public PushDialogModle pdm;
    /* access modifiers changed from: private */
    public String serVersion = "";
    /* access modifiers changed from: private */
    public String strSelect_Mainnum = "";
    /* access modifiers changed from: private */
    public String strSelect_Pairnum = "";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.callrecord_activity);
        callrecord_ctx = this;
        this.hostname = getString(R.string.hostname);
        this._appPrefs = new AppPreferences(this);
        this.pdm = new PushDialogModle(this);
        this.strSelect_Mainnum = this._appPrefs.getMainnum();
        this.strSelect_Pairnum = this._appPrefs.getPairnum();
        callrecord_lv = (ListView) findViewById(R.id.callrecord_listView);
        btn_callrecord_update = (Button) findViewById(R.id.btn_callrecord_update);
        btn_callrecord_command = (Button) findViewById(R.id.btn_callrecord_command);
        callrecord_update_time = (TextView) findViewById(R.id.callrecord_update_time);
        String serVersion_Got = this._appPrefs.getUserVersion();
        if (serVersion_Got == null || serVersion_Got.length() <= 0) {
            this.serVersion = ValueUtiles.UserVersion(this.hostname, this._appPrefs.getMainnum(), this._appPrefs.getpsw());
            this._appPrefs.saveUserVersion(this.serVersion);
        } else {
            this.serVersion = serVersion_Got;
        }
        if (this._appPrefs.getCallrecordFlag().equalsIgnoreCase("1") || !getStatus(this.strSelect_Pairnum).booleanValue()) {
            btn_callrecord_command.setEnabled(false);
            btn_callrecord_command.setBackgroundResource(R.drawable.command_img_down);
        } else {
            btn_callrecord_command.setEnabled(true);
            btn_callrecord_command.setBackgroundResource(R.drawable.command_img_up);
        }
        btn_callrecord_command.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                try {
                    if (!CallRecordViewActivity.this.haveInternet()) {
                        CallRecordViewActivity.btn_callrecord_command.setEnabled(false);
                        CallRecordViewActivity.btn_callrecord_command.setBackgroundResource(R.drawable.command_img_down);
                        if (MainControlActivity.statusMainImageObj != null) {
                            MainControlActivity.statusMainImageObj.setImageResource(R.drawable.offline);
                        }
                        if (MainControlActivity.statusPairImageObj != null) {
                            MainControlActivity.statusPairImageObj.setImageResource(R.drawable.offline);
                        }
                        CallRecordViewActivity.this.pdm.showDialog("訊息", "目前有離線或是網路不穩的狀況");
                    } else if (!CallRecordViewActivity.this.getStatus(CallRecordViewActivity.this.strSelect_Mainnum).booleanValue()) {
                        CallRecordViewActivity.btn_callrecord_command.setEnabled(false);
                        CallRecordViewActivity.btn_callrecord_command.setBackgroundResource(R.drawable.command_img_down);
                        if (MainControlActivity.statusMainImageObj != null) {
                            MainControlActivity.statusMainImageObj.setImageResource(R.drawable.offline);
                        }
                        CallRecordViewActivity.this.pdm.showDialog("訊息", "目前有離線或是網路不穩的狀況");
                    } else if (CallRecordViewActivity.this.getStatus(CallRecordViewActivity.this.strSelect_Pairnum).booleanValue()) {
                        CallRecordViewActivity.this._appPrefs.saveCallrecordFlag("1");
                        CallRecordViewActivity.btn_callrecord_command.setEnabled(false);
                        CallRecordViewActivity.btn_callrecord_command.setBackgroundResource(R.drawable.command_img_down);
                        MainControlActivity.sendMsg = new Message();
                        MainControlActivity.sendMsg.setBody("callrecord_update_start");
                        MainControlActivity.newChat.sendMessage(MainControlActivity.sendMsg);
                    } else {
                        CallRecordViewActivity.btn_callrecord_command.setEnabled(false);
                        CallRecordViewActivity.btn_callrecord_command.setBackgroundResource(R.drawable.command_img_down);
                        if (MainControlActivity.statusPairImageObj != null) {
                            MainControlActivity.statusPairImageObj.setImageResource(R.drawable.offline);
                        }
                        CallRecordViewActivity.this.pdm.showDialog("訊息", "目前有離線或是網路不穩的狀況");
                    }
                } catch (XMPPException e) {
                    CallRecordViewActivity.this.pdm.PushAlertSingleButton("連線訊息", "伺服器定期維護中，請稍候再試！！");
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        btn_callrecord_update.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                CallRecordViewActivity.this.callrecorddata = new CallRecordDataBean(CallRecordViewActivity.this, CallRecordViewActivity.this.serVersion, CallRecordViewActivity.this.strSelect_Pairnum);
                CallRecordViewActivity.this.callrecorddata.UpdataJason(CallRecordViewActivity.this.hostname);
            }
        });
        this.callrecorddata = new CallRecordDataBean(this, this.serVersion, this.strSelect_Pairnum);
        this.callrecorddata.UpdataJason(this.hostname);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this._appPrefs.saveCallrecordFlag("0");
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
    public Boolean getStatus(String strTarget) {
        if (new PresenceType(this).GetPresenceType(strTarget).booleanValue()) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }
}
