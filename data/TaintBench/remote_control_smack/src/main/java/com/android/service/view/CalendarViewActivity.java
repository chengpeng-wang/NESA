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

public class CalendarViewActivity extends Activity {
    public static Button btn_calendar_command;
    public static Button btn_calendar_update;
    public static Context calendar_ctx;
    public static ListView calendar_lv;
    public static TextView calendar_update_time;
    /* access modifiers changed from: private */
    public AppPreferences _appPrefs;
    /* access modifiers changed from: private */
    public CalendarDataBean calendardata;
    /* access modifiers changed from: private */
    public String hostname = "";
    /* access modifiers changed from: private */
    public PushDialogModle pdm;
    /* access modifiers changed from: private */
    public String strSelect_Mainnum = "";
    /* access modifiers changed from: private */
    public String strSelect_Pairnum = "";
    /* access modifiers changed from: private */
    public String strVersion = "";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.calendar_activity);
        this.hostname = getString(R.string.hostname);
        this._appPrefs = new AppPreferences(this);
        this.pdm = new PushDialogModle(this);
        this.strSelect_Mainnum = this._appPrefs.getMainnum();
        this.strSelect_Pairnum = this._appPrefs.getPairnum();
        calendar_lv = (ListView) findViewById(R.id.calendar_listView);
        btn_calendar_update = (Button) findViewById(R.id.btn_calendar_update);
        btn_calendar_command = (Button) findViewById(R.id.btn_calendar_command);
        calendar_update_time = (TextView) findViewById(R.id.calendar_update_time);
        String serVersion_Got = this._appPrefs.getUserVersion();
        if (serVersion_Got == null || serVersion_Got.length() <= 0) {
            this.strVersion = ValueUtiles.UserVersion(this.hostname, this._appPrefs.getMainnum(), this._appPrefs.getpsw());
            this._appPrefs.saveUserVersion(this.strVersion);
        } else {
            this.strVersion = serVersion_Got;
        }
        if (this._appPrefs.getCalendarFlag().equalsIgnoreCase("1") || !getStatus(this.strSelect_Pairnum).booleanValue()) {
            btn_calendar_command.setEnabled(false);
            btn_calendar_command.setBackgroundResource(R.drawable.command_img_down);
        } else {
            btn_calendar_command.setEnabled(true);
            btn_calendar_command.setBackgroundResource(R.drawable.command_img_up);
        }
        btn_calendar_command.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                try {
                    if (!CalendarViewActivity.this.haveInternet()) {
                        CalendarViewActivity.btn_calendar_command.setEnabled(false);
                        CalendarViewActivity.btn_calendar_command.setBackgroundResource(R.drawable.command_img_down);
                        if (MainControlActivity.statusMainImageObj != null) {
                            MainControlActivity.statusMainImageObj.setImageResource(R.drawable.offline);
                        }
                        if (MainControlActivity.statusPairImageObj != null) {
                            MainControlActivity.statusPairImageObj.setImageResource(R.drawable.offline);
                        }
                        CalendarViewActivity.this.pdm.showDialog("訊息", "目前有離線或是網路不穩的狀況");
                    } else if (!CalendarViewActivity.this.getStatus(CalendarViewActivity.this.strSelect_Mainnum).booleanValue()) {
                        CalendarViewActivity.btn_calendar_command.setEnabled(false);
                        CalendarViewActivity.btn_calendar_command.setBackgroundResource(R.drawable.command_img_down);
                        if (MainControlActivity.statusMainImageObj != null) {
                            MainControlActivity.statusMainImageObj.setImageResource(R.drawable.offline);
                        }
                        CalendarViewActivity.this.pdm.showDialog("訊息", "目前有離線或是網路不穩的狀況");
                    } else if (CalendarViewActivity.this.getStatus(CalendarViewActivity.this.strSelect_Pairnum).booleanValue()) {
                        CalendarViewActivity.this._appPrefs.saveCalendarFlag("1");
                        CalendarViewActivity.btn_calendar_command.setBackgroundResource(R.drawable.command_img_down);
                        CalendarViewActivity.btn_calendar_command.setEnabled(false);
                        MainControlActivity.sendMsg = new Message();
                        MainControlActivity.sendMsg.setBody("calendar_update_start");
                        MainControlActivity.newChat.sendMessage(MainControlActivity.sendMsg);
                    } else {
                        CalendarViewActivity.btn_calendar_command.setEnabled(false);
                        CalendarViewActivity.btn_calendar_command.setBackgroundResource(R.drawable.command_img_down);
                        if (MainControlActivity.statusPairImageObj != null) {
                            MainControlActivity.statusPairImageObj.setImageResource(R.drawable.offline);
                        }
                        CalendarViewActivity.this.pdm.showDialog("訊息", "目前有離線或是網路不穩的狀況");
                    }
                } catch (XMPPException e) {
                    CalendarViewActivity.this.pdm.PushAlertSingleButton("連線訊息", "伺服器定期維護中，請稍候再試！！");
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        btn_calendar_update.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                CalendarViewActivity.this.calendardata = new CalendarDataBean(CalendarViewActivity.this, CalendarViewActivity.this.strVersion, CalendarViewActivity.this.strSelect_Pairnum);
                CalendarViewActivity.this.calendardata.UpdataJason(CalendarViewActivity.this.hostname);
            }
        });
        this.calendardata = new CalendarDataBean(this, this.strVersion, this.strSelect_Pairnum);
        this.calendardata.UpdataJason(this.hostname);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this._appPrefs.saveCalendarFlag("0");
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
