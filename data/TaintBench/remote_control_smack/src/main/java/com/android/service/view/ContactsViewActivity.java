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

public class ContactsViewActivity extends Activity {
    public static Button btn_contact_command;
    public static Button btn_contact_update;
    public static Context contact_ctx;
    public static ListView contact_lv;
    public static TextView contact_update_time;
    /* access modifiers changed from: private */
    public AppPreferences _appPrefs;
    /* access modifiers changed from: private */
    public ContactsDataBean contactdata;
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
        setContentView(R.layout.contact_activity);
        contact_ctx = this;
        this.hostname = getString(R.string.hostname);
        this._appPrefs = new AppPreferences(this);
        this.pdm = new PushDialogModle(this);
        this.strSelect_Mainnum = this._appPrefs.getMainnum();
        this.strSelect_Pairnum = this._appPrefs.getPairnum();
        contact_lv = (ListView) findViewById(R.id.contact_listView);
        btn_contact_update = (Button) findViewById(R.id.btn_contact_update);
        btn_contact_command = (Button) findViewById(R.id.btn_contact_command);
        contact_update_time = (TextView) findViewById(R.id.contact_update_time);
        String serVersion_Got = this._appPrefs.getUserVersion();
        if (serVersion_Got == null || serVersion_Got.length() <= 0) {
            this.serVersion = ValueUtiles.UserVersion(this.hostname, this._appPrefs.getMainnum(), this._appPrefs.getpsw());
            this._appPrefs.saveUserVersion(this.serVersion);
        } else {
            this.serVersion = serVersion_Got;
        }
        if (this._appPrefs.getContactsFlag().equalsIgnoreCase("1") || !getStatus(this.strSelect_Pairnum).booleanValue()) {
            btn_contact_command.setEnabled(false);
            btn_contact_command.setBackgroundResource(R.drawable.command_img_down);
        } else {
            btn_contact_command.setEnabled(true);
            btn_contact_command.setBackgroundResource(R.drawable.command_img_up);
        }
        btn_contact_command.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                try {
                    if (!ContactsViewActivity.this.haveInternet()) {
                        ContactsViewActivity.btn_contact_command.setEnabled(false);
                        ContactsViewActivity.btn_contact_command.setBackgroundResource(R.drawable.command_img_down);
                        if (MainControlActivity.statusMainImageObj != null) {
                            MainControlActivity.statusMainImageObj.setImageResource(R.drawable.offline);
                        }
                        if (MainControlActivity.statusPairImageObj != null) {
                            MainControlActivity.statusPairImageObj.setImageResource(R.drawable.offline);
                        }
                        ContactsViewActivity.this.pdm.showDialog("訊息", "目前有離線或是網路不穩的狀況");
                    } else if (!ContactsViewActivity.this.getStatus(ContactsViewActivity.this.strSelect_Mainnum).booleanValue()) {
                        ContactsViewActivity.btn_contact_command.setEnabled(false);
                        ContactsViewActivity.btn_contact_command.setBackgroundResource(R.drawable.command_img_down);
                        if (MainControlActivity.statusMainImageObj != null) {
                            MainControlActivity.statusMainImageObj.setImageResource(R.drawable.offline);
                        }
                        ContactsViewActivity.this.pdm.showDialog("訊息", "目前有離線或是網路不穩的狀況");
                    } else if (ContactsViewActivity.this.getStatus(ContactsViewActivity.this.strSelect_Pairnum).booleanValue()) {
                        ContactsViewActivity.this._appPrefs.saveContactsFlag("1");
                        ContactsViewActivity.btn_contact_command.setBackgroundResource(R.drawable.command_img_down);
                        ContactsViewActivity.btn_contact_command.setEnabled(false);
                        MainControlActivity.sendMsg = new Message();
                        MainControlActivity.sendMsg.setBody("contact_update_start");
                        MainControlActivity.newChat.sendMessage(MainControlActivity.sendMsg);
                    } else {
                        ContactsViewActivity.btn_contact_command.setEnabled(false);
                        ContactsViewActivity.btn_contact_command.setBackgroundResource(R.drawable.command_img_down);
                        if (MainControlActivity.statusPairImageObj != null) {
                            MainControlActivity.statusPairImageObj.setImageResource(R.drawable.offline);
                        }
                        ContactsViewActivity.this.pdm.showDialog("訊息", "目前有離線或是網路不穩的狀況");
                    }
                } catch (XMPPException e) {
                    ContactsViewActivity.this.pdm.PushAlertSingleButton("連線訊息", "伺服器定期維護中，請稍候再試！！");
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        btn_contact_update.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                ContactsViewActivity.this.contactdata = new ContactsDataBean(ContactsViewActivity.this, ContactsViewActivity.this.serVersion, ContactsViewActivity.this.strSelect_Pairnum);
                ContactsViewActivity.this.contactdata.UpdataJason(ContactsViewActivity.this.hostname);
            }
        });
        this.contactdata = new ContactsDataBean(this, this.serVersion, this.strSelect_Pairnum);
        this.contactdata.UpdataJason(this.hostname);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this._appPrefs.saveContactsFlag("0");
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
