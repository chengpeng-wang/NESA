package com.android.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.xmpp.client.util.AppPreferences;

public class GetPackageBroadcast extends BroadcastReceiver {
    private AppPreferences _appPrefs;

    public void onReceive(Context context, Intent intent) {
        this._appPrefs = new AppPreferences(context);
        if ("android.intent.action.PACKAGE_REPLACED".equals(intent.getAction())) {
            String roleString = this._appPrefs.getBody();
            System.out.println("Role------------" + roleString);
            if (roleString.equalsIgnoreCase("b")) {
                Intent xmpp_service = new Intent(context, XmppService.class);
                xmpp_service.setFlags(268435456);
                context.startService(xmpp_service);
            }
        }
    }
}
