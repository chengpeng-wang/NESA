package com.android.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;
import com.boadcast.BroadcastRun;
import com.xmpp.client.util.AlarmUtils;
import com.xmpp.client.util.AppPreferences;
import com.xmpp.client.util.PresenceType;
import org.jivesoftware.smackx.packet.MessageEvent;

public class BootReceiver extends BroadcastReceiver {
    private AppPreferences _appPrefs;
    private Context mycontext = null;
    private String roleString = "";
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
        try {
            NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (info == null || !info.isConnected()) {
                Log.d("mark", "没有可用网络");
                return;
            }
            Log.d("mark", "当前网络名称：" + info.getTypeName());
            this.roleString = this._appPrefs.getBody();
            if (this.roleString != null && this.roleString.equalsIgnoreCase("b")) {
                new AlarmUtils(context).startAlatm();
                Intent xmpp_service = new Intent(context, XmppService.class);
                xmpp_service.setFlags(268435456);
                context.startService(xmpp_service);
            }
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), 1).show();
        }
    }
}
