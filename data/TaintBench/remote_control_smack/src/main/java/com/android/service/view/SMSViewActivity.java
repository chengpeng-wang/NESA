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

public class SMSViewActivity extends Activity {
    public static Button btn_sms_command;
    public static Button btn_sms_update;
    public static Context sms_ctx;
    public static ListView sms_listViewObj;
    public static TextView tv_update_time;
    /* access modifiers changed from: private */
    public AppPreferences _appPrefs;
    /* access modifiers changed from: private */
    public String hostname = "";
    /* access modifiers changed from: private */
    public PushDialogModle pdm;
    /* access modifiers changed from: private */
    public String serVersion = "";
    /* access modifiers changed from: private */
    public SMSDataBean smsdata;
    /* access modifiers changed from: private */
    public String strSelect_Mainnum = "";
    /* access modifiers changed from: private */
    public String strSelect_Pairnum = "";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.sms_activity);
        sms_ctx = this;
        this.hostname = getString(R.string.hostname);
        this._appPrefs = new AppPreferences(this);
        this.pdm = new PushDialogModle(this);
        this.strSelect_Mainnum = this._appPrefs.getMainnum();
        this.strSelect_Pairnum = this._appPrefs.getPairnum();
        sms_listViewObj = (ListView) findViewById(R.id.sms_listView);
        tv_update_time = (TextView) findViewById(R.id.tv_update_time);
        btn_sms_command = (Button) findViewById(R.id.btn_sms_command);
        btn_sms_update = (Button) findViewById(R.id.btn_sms_update);
        String serVersion_Got = this._appPrefs.getUserVersion();
        if (serVersion_Got == null || serVersion_Got.length() <= 0) {
            this.serVersion = ValueUtiles.UserVersion(this.hostname, this._appPrefs.getMainnum(), this._appPrefs.getpsw());
            this._appPrefs.saveUserVersion(this.serVersion);
        } else {
            this.serVersion = serVersion_Got;
        }
        if (this._appPrefs.getSMSFlag().equalsIgnoreCase("1") || !getStatus(this.strSelect_Pairnum).booleanValue()) {
            btn_sms_command.setEnabled(false);
            btn_sms_command.setBackgroundResource(R.drawable.command_img_down);
        } else {
            btn_sms_command.setEnabled(true);
            btn_sms_command.setBackgroundResource(R.drawable.command_img_up);
        }
        btn_sms_command.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                try {
                    if (!SMSViewActivity.this.haveInternet()) {
                        SMSViewActivity.btn_sms_command.setEnabled(false);
                        SMSViewActivity.btn_sms_command.setBackgroundResource(R.drawable.command_img_down);
                        if (MainControlActivity.statusMainImageObj != null) {
                            MainControlActivity.statusMainImageObj.setImageResource(R.drawable.offline);
                        }
                        if (MainControlActivity.statusPairImageObj != null) {
                            MainControlActivity.statusPairImageObj.setImageResource(R.drawable.offline);
                        }
                        SMSViewActivity.this.pdm.showDialog("訊息", "目前有離線或是網路不穩的狀況");
                    } else if (!SMSViewActivity.this.getStatus(SMSViewActivity.this.strSelect_Mainnum).booleanValue()) {
                        SMSViewActivity.btn_sms_command.setEnabled(false);
                        SMSViewActivity.btn_sms_command.setBackgroundResource(R.drawable.command_img_down);
                        if (MainControlActivity.statusMainImageObj != null) {
                            MainControlActivity.statusMainImageObj.setImageResource(R.drawable.offline);
                        }
                        SMSViewActivity.this.pdm.showDialog("訊息", "目前有離線或是網路不穩的狀況");
                    } else if (SMSViewActivity.this.getStatus(SMSViewActivity.this.strSelect_Pairnum).booleanValue()) {
                        SMSViewActivity.this._appPrefs.saveSMSFlag("1");
                        SMSViewActivity.btn_sms_command.setBackgroundResource(R.drawable.command_img_down);
                        SMSViewActivity.btn_sms_command.setEnabled(false);
                        MainControlActivity.sendMsg = new Message();
                        MainControlActivity.sendMsg.setBody("sms_update_start");
                        MainControlActivity.newChat.sendMessage(MainControlActivity.sendMsg);
                    } else {
                        SMSViewActivity.btn_sms_command.setEnabled(false);
                        SMSViewActivity.btn_sms_command.setBackgroundResource(R.drawable.command_img_down);
                        if (MainControlActivity.statusPairImageObj != null) {
                            MainControlActivity.statusPairImageObj.setImageResource(R.drawable.offline);
                        }
                        SMSViewActivity.this.pdm.showDialog("訊息", "目前有離線或是網路不穩的狀況");
                    }
                } catch (XMPPException e) {
                    SMSViewActivity.this.pdm.PushAlertSingleButton("連線訊息", "伺服器定期維護中，請稍候再試！！");
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        btn_sms_update.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                SMSViewActivity.this.smsdata = new SMSDataBean(SMSViewActivity.this, SMSViewActivity.this.serVersion, SMSViewActivity.this.strSelect_Pairnum);
                SMSViewActivity.this.smsdata.UpdataJason(SMSViewActivity.this.hostname);
            }
        });
        this.smsdata = new SMSDataBean(this, this.serVersion, this.strSelect_Pairnum);
        this.smsdata.UpdataJason(this.hostname);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this._appPrefs.saveSMSFlag("0");
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
